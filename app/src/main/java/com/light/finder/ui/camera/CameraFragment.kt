package com.light.finder.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.common.util.concurrent.ListenableFuture
import com.light.domain.model.Message
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.PermissionRequester
import com.light.finder.common.VisibilityCallBack
import com.light.finder.di.modules.CameraComponent
import com.light.finder.di.modules.CameraModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.presentation.BuildConfig
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.CameraViewModel
import com.light.presentation.viewmodels.CameraViewModel.*
import kotlinx.android.synthetic.main.camera_ui_container.*
import kotlinx.android.synthetic.main.camera_ui_container.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.layout_permission.*
import kotlinx.android.synthetic.main.layout_preview.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class CameraFragment : BaseFragment() {

    private lateinit var component: CameraComponent
    private val viewModel: CameraViewModel by lazy { getViewModel { component.cameraViewModel } }
    private lateinit var cameraPermissionRequester: PermissionRequester
    private lateinit var visibilityCallBack: VisibilityCallBack
    private lateinit var alertDialog: AlertDialog
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val timer = object : CountDownTimer(INIT_INTERVAL, DOWN_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            countDownText.text = "${millisUntilFinished / 1000}"
        }

        override fun onFinish() {

        }
    }

    companion object {
        private const val TAG = "CameraX"
        private const val INIT_INTERVAL = 5000L
        private const val DOWN_INTERVAL = 1000L
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private var flashMode = ImageCapture.FLASH_MODE_OFF
    }


    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var mainExecutor: Executor

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainExecutor = ContextCompat.getMainExecutor(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_camera, container, false)


    private val imageSavedListener = object : ImageCapture.OnImageSavedCallback {
        override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
            Timber.e("$TAG Photo capture failed: $message cause")
        }

        @SuppressLint("ObsoleteSdkInt")
        override fun onImageSaved(photoFile: File) {
            Timber.d("$TAG Photo capture succeeded: ${photoFile.absolutePath}")

            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(photoFile.extension)
            MediaScannerConnection.scanFile(
                context, arrayOf(photoFile.absolutePath), arrayOf(mimeType), null
            )
            viewModel.onSendButtonClicked(photoFile.absolutePath)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = app.applicationComponent.plus(CameraModule())
            cameraPermissionRequester = PermissionRequester(this, Manifest.permission.CAMERA)
        } ?: throw Exception("Invalid Activity")

        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)

        viewModel.model.observe(viewLifecycleOwner, Observer(::observeUpdateUI))
        viewModel.modelPreview.observe(viewLifecycleOwner, Observer(::observePreviewView))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeModelContent))
        viewModel.modelRequestCancel.observe(viewLifecycleOwner, Observer(::observeCancelRequest))
        viewModel.modelError.observe(viewLifecycleOwner, Observer(::observeErrorResponse))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeDialogButtonAction))


        broadcastManager = LocalBroadcastManager.getInstance(view.context)

    }


    private fun observeUpdateUI(model: UiModel) {
        when (model) {
            is UiModel.CameraInitializeScreen -> {
                viewModel.onPermissionsViewRequested(checkSelfCameraPermission())
            }

            is UiModel.PermissionsViewRequested -> {
                setPermissionView()
            }

            is UiModel.RequestCameraViewDisplay -> cameraPermissionRequester.request { isPermissionGranted ->
                viewModel.onCameraPermissionRequested(isPermissionGranted)
            }

            is UiModel.CameraViewDisplay -> setCameraSpecs()
            is UiModel.CameraViewPermissionDenied -> showErrorDialog(
                getString(R.string.enable_camera_access),
                        getString (R.string.enable_subtitle),
                        getString (R.string.enable_camera_button),
                true
            )
        }
    }

    private fun observeCancelRequest(cancelModelEvent: Event<CancelModel>) {
        cancelModelEvent.getContentIfNotHandled()?.let {
            timer.onTick(INIT_INTERVAL)
            timer.cancel()
            layoutPreview.gone()
            layoutCamera.visible()
            cameraUiContainer.visible()
            visibilityCallBack.onVisibilityChanged(false)
            initializeLottieAnimation()
        }
    }

    private fun observeErrorResponse(modelErrorEvent: Event<ErrorModel>) {
        modelErrorEvent.getContentIfNotHandled()?.let { errorModel ->
            when (errorModel) {
                is ErrorModel.TimeOutError -> {
                    showErrorDialog(
                        getString(R.string.unidentified),
                        getString(R.string.unidentified_sub),
                        getString(R.string.try_again),
                        false
                    )
                }

                is ErrorModel.NotBulbIdentified -> {
                    showErrorDialog(
                        getString(R.string.unidentified),
                        getString(R.string.unidentified_sub),
                        getString(R.string.try_again),
                        false
                    )
                }

                is ErrorModel.ServerError -> {
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.error_sub),
                        getString(R.string.ok),
                        false

                    )
                }
            }
        }
    }

    private fun observeDialogButtonAction(modelDialogEvent: Event<DialogModel>) {
        modelDialogEvent.getContentIfNotHandled()?.let { dialogModel ->
            when (dialogModel) {
                is DialogModel.PositiveButton -> {
                    when(dialogModel.message){
                        "retry" ->  revertCameraView()
                        "enable" -> deepLinkToSettings()
                    }

                    alertDialog.dismiss()
                }

                is DialogModel.SecondaryButton -> {//TODO
                }
            }
        }
    }

    private fun observeModelContent(modelContent: Content) {
        when (modelContent) {
            is Content.EncodeImage -> {
                imageViewPreview.loadFile(File(modelContent.absolutePath))
                viewModel.onRequestFileImageEncoded(modelContent.absolutePath)
            }
            is Content.RequestCategoriesMessages -> {
                timer.start()
                viewModel.onRequestCategoriesMessages(modelContent.encodedImage)
            }
            is Content.RequestModelContent -> navigateToCategories(modelContent.messages)
        }
    }

    private fun observePreviewView(previewModel: Event<PreviewModel>) {
        previewModel.getContentIfNotHandled()?.let {
            clearPreviousImage()
            layoutCamera.gone()
            layoutPermission.gone()
            browseButton.gone()
            layoutPreview.visible()
            cameraUiContainer.gone()
            visibilityCallBack.onVisibilityChanged(true)

            cancelButton.setOnClickListener {
                viewModel.onCancelRequest()
            }

        }
    }

    private fun observeFlashButtonAction(flashModel: FlashModel) {
        flashMode = when (flashModel) {
            is FlashModel.ModeOn -> {
                flashSwitchButton.setImageResource(R.drawable.ic_flash_on)
                ImageCapture.FLASH_MODE_ON
            }
            is FlashModel.ModeOff -> {
                flashSwitchButton.setImageResource(R.drawable.ic_flash_off)
                ImageCapture.FLASH_MODE_OFF
            }
        }
        imageCapture?.flashMode = flashMode

    }

    private fun revertCameraView() {
        timer.onTick(INIT_INTERVAL)
        layoutPreview.gone()
        layoutCamera.visible()
        browseButton.visible()
        cameraUiContainer.visible()
        visibilityCallBack.onVisibilityChanged(false)

        lottieAnimationView.playAnimation() //restore lottie view again after being consumed
        initializeLottieAnimation()
    }

    private fun deepLinkToSettings() {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }


    private fun setPermissionView() {
        layoutCamera.gone()
        browseButton.visible()
        layoutPermission.visible()
        textViewEnableAccess.setOnClickListener {
            viewModel.onRequestCameraViewDisplay()
        }
    }

    private fun navigateToCategories(content: Event<List<Message>>) {
        content.getContentIfNotHandled()?.let { messages ->
            timer.cancel()
            mFragmentNavigation.pushFragment(CategoriesFragment.newInstance(messages[0]))
        }
    }


    private fun showErrorDialog(
        titleDialog: String,
        subtitleDialog: String,
        buttonPositiveText: String,
        neutralButton: Boolean = true
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        timer.cancel()
        lottieAnimationView.pauseAnimation()
        dialogView.buttonPositive.text = buttonPositiveText
        dialogView.textViewTitleDialog.text = titleDialog
        dialogView.textViewSubTitleDialog.text = subtitleDialog
        dialogView.buttonPositive.setOnClickListener {
            if (neutralButton) {
                viewModel.onPositiveAlertDialogButtonClicked("enable")
            } else {
                viewModel.onPositiveAlertDialogButtonClicked("retry")
            }

        }

        dialogView.buttonNegative.gone()
        if (neutralButton) {
            dialogView.buttonNeutral.text = getString(R.string.not_now)
            dialogView.buttonNeutral.setOnClickListener {
                alertDialog.dismiss()
            }
        } else {
            dialogView.buttonNeutral.gone()
        }
        alertDialog.show()

    }


    private fun setCameraSpecs() {
        layoutCamera.visible()
        layoutPermission.gone()

        outputDirectory = CameraActivity.getOutputDirectory(requireContext())

        viewFinder.post {

            displayId = viewFinder.display.displayId


            initCameraUi()

            flashSwitchButton.setOnClickListener {
                viewModel.onFlashModeButtonClicked(flashMode)
            }

            //TODO check this and move it to local data source
            lifecycleScope.launch(Dispatchers.IO) {
                outputDirectory.listFiles { file ->
                    EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
                }
            }
        }
    }


    private fun initCameraUi() {

        container.findViewById<ConstraintLayout>(R.id.cameraUiContainer)?.let {
            container.removeView(it)
        }

        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        controls.cameraCaptureButton.setSafeOnClickListener {
            imageCapture?.let { imageCapture ->
                val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
                val metadata = ImageCapture.Metadata().apply {

                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                imageCapture.takePicture(photoFile, metadata, mainExecutor, imageSavedListener)

                container.postDelayed({
                    container.foreground = ColorDrawable(Color.WHITE)
                    container.postDelayed(
                        { container.foreground = null }, ANIMATION_FAST_MILLIS
                    )
                    viewModel.onCameraButtonClicked()
                }, ANIMATION_SLOW_MILLIS)

            }
        }


        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        /**
         * CAMERA USE-CASES
         */
        val rotation = viewFinder.display.rotation

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()


        preview = Preview.Builder()
            // .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            //.setTargetAspectRatio(screenAspectRatio)
            .setFlashMode(flashMode)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            //.setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(mainExecutor,
                    LuminosityAnalyzer { luma ->
                        Timber.d("$TAG Average luminosity: $luma")
                    })
            }

        /**
         *END USE-CASES
         */

        preview?.previewSurfaceProvider = viewFinder.previewSurfaceProvider

        cameraProvider.unbindAll()

        camera = cameraProvider.bindToLifecycle(
            this as LifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
        )

        /**
         * once camera container is initialize we start observing camera events from camera viewmodels
         */
        viewModel.modelFlash.observe(viewLifecycleOwner, Observer(::observeFlashButtonAction))

    }

    //TODO set this method for extension
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

    //TODO set this method for extension
    private fun initializeLottieAnimation() {
        lottieAnimationView.progress = 0.0f
    }

    //TODO set this method for extension
    private fun clearPreviousImage() {
        imageViewPreview.setImageDrawable(null)//we clear the view so we it won't keep  old images

    }

}
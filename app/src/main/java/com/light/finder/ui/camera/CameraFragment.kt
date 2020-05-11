package com.light.finder.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.light.domain.model.ParsingError
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.PermissionRequester
import com.light.finder.common.ActivityCallback
import com.light.finder.data.source.local.ImageRepository
import com.light.finder.data.source.remote.reports.CrashlyticsException
import com.light.finder.di.modules.submodules.CameraComponent
import com.light.finder.di.modules.submodules.CameraModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
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
    private val imageRepository: ImageRepository by lazy { component.imageRepository }
    private lateinit var cameraPermissionRequester: PermissionRequester
    private lateinit var connectivityRequester: ConnectivityRequester
    private lateinit var activityCallback: ActivityCallback
    private lateinit var alertDialog: AlertDialog
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var modelUiState: ModelStatus = ModelStatus.FEED


    val timer = object : CountDownTimer(INIT_INTERVAL, DOWN_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            val timeText = millisUntilFinished / 1000
            if (timeText > 0) {
                countDownText.text = timeText.toString()
            }
        }

        override fun onFinish() {

        }
    }

    companion object {
        private const val TAG = "CameraX"
        private const val INIT_INTERVAL = 6000L
        private const val DOWN_INTERVAL = 1000L
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val ANIMATION_FAST_MILLIS = 50L
        private const val ANIMATION_SLOW_MILLIS = 100L
        private const val TIME_OUT_LOG_REPORT = 408
        private const val PARSE_ERROR_LOG_REPORT = 422

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

    private var isComingFromSettings: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainExecutor = ContextCompat.getMainExecutor(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as ActivityCallback
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


    private val imageCaptureListener = object : ImageCapture.OnImageCapturedCallback() {
        override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
            Timber.e("$TAG Photo capture failed: $message cause")
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun onCaptureSuccess(image: ImageProxy) {
            viewModel.onCameraButtonClicked(imageRepository.getBitmap(image.image!!))
            image.close()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = lightFinderComponent.plus(CameraModule())
            cameraPermissionRequester = PermissionRequester(this, Manifest.permission.CAMERA)
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")

        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)

        viewModel.model.observe(viewLifecycleOwner, Observer(::observeUpdateUI))
        viewModel.modelPreview.observe(viewLifecycleOwner, Observer(::observePreviewView))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeModelContent))
        viewModel.modelRequestCancel.observe(viewLifecycleOwner, Observer(::observeCancelRequest))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeErrorResponse))
        viewModel.modelResponseDialog.observe(
            viewLifecycleOwner,
            Observer(::observeDialogButtonAction)

        )


        requestItemCount()
        broadcastManager = LocalBroadcastManager.getInstance(view.context)


    }

    private fun requestItemCount() = viewModel.onRequestGetItemCount()

    private fun observeItemCount(itemCount: RequestModelItemCount) {
        val itemQuantity = itemCount.itemCount.peekContent().itemQuantity
        when {
            itemQuantity > 0 ->
                activityCallback.onBadgeCountChanged(itemQuantity)
            else -> Timber.d("egee Cart is empty")
        }
    }

    override fun onResume() {
        super.onResume()
        if (isComingFromSettings) {
            viewModel.onPermissionsViewRequested(checkSelfCameraPermission())
            isComingFromSettings = false
        }
    }


    private fun observeUpdateUI(model: UiModel) {
        when (model) {
            is UiModel.CameraInitializeScreen -> {
                viewModel.onPermissionsViewRequested(checkSelfCameraPermission())
            }

            is UiModel.PermissionsViewRequested -> {
                modelUiState = ModelStatus.PERMISSION
                screenNavigator.toCameraPermissionScreen(this)
                setPermissionView()
            }

            is UiModel.RequestCameraViewDisplay -> cameraPermissionRequester.request({ isPermissionGranted ->
                viewModel.onCameraPermissionRequested(isPermissionGranted)
            }, (::observeDenyPermission))

            is UiModel.CameraViewDisplay -> {
                modelUiState = ModelStatus.FEED
                screenNavigator.toCameraFeedScreen(this)
                setCameraSpecs()
            }
        }
    }

    private fun observeCancelRequest(cancelModelEvent: Event<CancelModel>) {
        cancelModelEvent.getContentIfNotHandled()?.let {
            //timer.onTick(INIT_INTERVAL)
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.cancel_identified_event)) {}
            timer.cancel()
            layoutPreview.gone()
            layoutCamera.visible()
            cameraUiContainer.visible()
            activityCallback.onVisibilityChanged(false)
            initializeLottieAnimation()
        }
    }

    private fun observeErrorResponse(modelErrorEvent: Event<DialogModel>) {
        modelUiState = ModelStatus.FEED
        screenNavigator.toCameraFeedScreen(this)
        modelErrorEvent.getContentIfNotHandled()?.let { errorModel ->
            when (errorModel) {
                is DialogModel.TimeOutError -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.time_out_event)
                        )
                    }
                    CrashlyticsException(TIME_OUT_LOG_REPORT, null, null).logException()
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.timeout_sub),
                        getString(R.string.ok),
                        false
                    )
                }

                is DialogModel.NotBulbIdentified -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.no_lightbulb_identified_event)
                        )
                    }
                    showNoBulbErrorDialog(
                        getString(R.string.unidentified),
                        getString(R.string.unidentified_sub),
                        getString(R.string.try_again)
                    )
                }

                is DialogModel.ServerError -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.api_server_error_event)
                        )
                    }
                    if (errorModel.exception is ParsingError) {
                        CrashlyticsException(
                            PARSE_ERROR_LOG_REPORT,
                            errorModel.errorMessage,
                            null
                        ).logException()
                    }
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.error_sub),
                        getString(R.string.ok),
                        false

                    )
                }

                is DialogModel.PermissionPermanentlyDenied -> {
                    showErrorDialog(
                        getString(R.string.enable_camera_access),
                        getString(R.string.enable_subtitle),
                        getString(R.string.enable_camera_button),
                        true
                    )

                }
            }
        }
    }

    private fun observeDialogButtonAction(modelDialogEvent: Event<ResponseDialogModel>) {
        modelDialogEvent.getContentIfNotHandled()?.let { dialogModel ->
            when (dialogModel) {
                is ResponseDialogModel.PositiveButton -> {
                    when (dialogModel.message) {
                        "retry" -> revertCameraView()
                        "enable" -> deepLinkToSettings()
                    }

                    alertDialog.dismiss()
                }

                is ResponseDialogModel.SecondaryButton -> {//TODO
                }
            }
        }
    }

    private fun observeModelContent(modelContent: Content) {
        when (modelContent) {
            is Content.EncodeImage -> imageRepository.convertImageToBase64(
                modelContent.bitmap,
                ::handleResultBase64
            )
            is Content.RequestCategoriesMessages -> {
                viewModel.onRequestCategoriesMessages(modelContent.encodedImage)
            }
            is Content.RequestModelContent -> {
                navigateToCategories(modelContent.messages)
            }
        }
    }


    private fun observePreviewView(previewModel: Event<PreviewModel>) {
        previewModel.getContentIfNotHandled()?.let {
            layoutCamera.gone()
            layoutPermission.gone()
            //browseButton.gone()
            cameraUiContainer.gone()//TODO change the order of this visibility
            layoutPreview.visible()
            imageViewPreview.loadImage(it.bitmap)
            //start countdown
            timer.start()
            modelUiState = ModelStatus.LOADING
            screenNavigator.toCameraLoading(this)
            activityCallback.onVisibilityChanged(true)

            cancelButton.setOnClickListener {
                viewModel.onCancelRequest()
            }
        }
    }

    fun getStatusView(): ModelStatus = modelUiState

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

    private fun observeDenyPermission(isPermanentlyDenied: Boolean) {
        viewModel.onPermissionDenied(isPermanentlyDenied)
    }

    private fun handleResultBase64(base64: String) {
        viewModel.onRequestCategoriesMessages(base64)
    }

    private fun revertCameraView() {
        layoutPreview.gone()
        layoutCamera.visible()
        cameraUiContainer.visible()
        activityCallback.onVisibilityChanged(false)

        lottieAnimationView.playAnimation() //restore lottie view again after being consumed
        initializeLottieAnimation()
    }

    private fun deepLinkToSettings() {
        isComingFromSettings = true
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }


    private fun setPermissionView() {
        layoutCamera.gone()
        //browseButton.visible()
        layoutPermission.visible()
        enableContainer.setOnClickListener {
            viewModel.onRequestCameraViewDisplay()
        }
    }

    private fun navigateToCategories(content: Event<List<Message>>) {
        content.getContentIfNotHandled()?.let { messages ->
            timer.cancel()
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.lightbulb_identified_event)) {
                putString(getString(R.string.base), messages[0].categories[0].categoryProductBase)
                putString(getString(R.string.shape), messages[0].categories[0].categoryShape)
            }
            // we set status of type FEED so next time we access this page it will have the FEED value assigned by default
            modelUiState = ModelStatus.FEED
            screenNavigator.navigateToCategoriesScreen(messages[0])
        }
    }


    //TODO make this Error Dialog Generic to the App
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
        alertDialog.window?.setDimAmount(0.6f)
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


    private fun showNoBulbErrorDialog(
        titleDialog: String,
        subtitleDialog: String,
        buttonPositiveText: String
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        timer.cancel()
        lottieAnimationView.pauseAnimation()
        dialogView.buttonPositive.text = buttonPositiveText
        dialogView.textViewTitleDialog.text = titleDialog
        dialogView.textViewSubTitleDialog.text = subtitleDialog
        dialogView.buttonPositive.setOnClickListener {
            viewModel.onPositiveAlertDialogButtonClicked("retry")
        }

        dialogView.buttonNegative.gone()

        dialogView.buttonNeutral.text = getString(R.string.help_me_scan)
        dialogView.buttonNeutral.setOnClickListener {
            alertDialog.dismiss()
            revertCameraView()
            screenNavigator.navigateToTipsAndTricksScreen()
        }

        alertDialog.show()

    }


    private fun setCameraSpecs() {
        layoutCamera.visible()
        layoutPermission.gone()


        outputDirectory = CameraLightFinderActivity.getOutputDirectory(requireContext())

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

    fun onCameraCaptureClick() {
        imageCapture?.let { imageCapture ->
            val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
            val metadata = ImageCapture.Metadata().apply {

                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }
            imageCapture.takePicture(mainExecutor, imageCaptureListener)

            container.postDelayed({
                container.foreground = ColorDrawable(Color.WHITE)
                container.postDelayed(
                    { container.foreground = null }, ANIMATION_FAST_MILLIS
                )
            }, ANIMATION_SLOW_MILLIS)

        }
    }

    private fun initCameraUi() {

        container.findViewById<ConstraintLayout>(R.id.cameraUiContainer)?.let {
            container.removeView(it)
        }

        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        controls.cameraCaptureButton.setSafeOnClickListener {
            connectivityRequester.checkConnection { isConnected ->
                if (isConnected) {
                    firebaseAnalytics.logEventOnGoogleTagManager("send_photo") {
                        putBoolean("flash_enable", flashMode == ImageCapture.FLASH_MODE_ON)
                    }
                    onCameraCaptureClick()
                } else {
                    activityCallback.onInternetConnectionLost()
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_lightbulb_identified_event)) {
                        putString(
                            getString(R.string.error_reason_event),
                            getString(R.string.no_internet_connection_event)
                        )
                    }
                }
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

        helpButton.setOnClickListener {
            //todo uncomment for 1.0
            //firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.photo_help)) {}
            screenNavigator.navigateToTipsAndTricksScreen()

        }

        /**
         * once camera container is initialize we start observing camera events from camera viewmodels
         */
        viewModel.modelFlash.observe(viewLifecycleOwner, Observer(::observeFlashButtonAction))

    }

    //TODO set this method for extension when media user is implemented
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

    //TODO set this method for extension
    private fun initializeLottieAnimation() {
        lottieAnimationView.progress = 0.0f
    }

}

enum class ModelStatus { FEED, LOADING, PERMISSION }
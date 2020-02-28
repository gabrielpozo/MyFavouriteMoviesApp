package com.light.finder.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.light.domain.model.Message
import com.light.finder.BuildConfig
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.PermissionRequester
import com.light.finder.di.modules.CameraComponent
import com.light.finder.di.modules.CameraModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.CameraViewModel
import com.light.presentation.viewmodels.CameraViewModel.*
import kotlinx.android.synthetic.main.camera_ui_container.*
import kotlinx.android.synthetic.main.camera_ui_container.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.layout_permission.*
import kotlinx.android.synthetic.main.layout_preview.*
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

    companion object {
        const val TAG = "CameraX"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private var flashMode = ImageCapture.FLASH_MODE_OFF
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }


    private lateinit var container: ConstraintLayout
    lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager
    lateinit var mainExecutor: Executor

    private var displayId: Int = -1
    var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    var preview: Preview? = null
    var imageCapture: ImageCapture? = null
    var imageAnalyzer: ImageAnalysis? = null
    var camera: Camera? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainExecutor = ContextCompat.getMainExecutor(requireContext())
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

        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUI))
        viewModel.modelPreview.observe(viewLifecycleOwner, Observer(::setPreviewView))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeModelContent))
        viewModel.modelRequestCancel.observe(viewLifecycleOwner, Observer(::observeCancelRequest))

        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

    }

    private fun observeCancelRequest(cancelModelEvent: Event<CancelModel>) {
        cancelModelEvent.getContentIfNotHandled()?.let {
            layoutPreview.gone()
            layoutCamera.visible()
            cameraUiContainer.visible()
            // lottieAnimationView.cancelAnimation()
        }
    }


    private fun updateUI(model: UiModel) {
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
            is UiModel.CameraViewPermissionDenied -> deepLinkToSettings()
        }
    }


    private fun deepLinkToSettings() {
        startActivity(
            Intent(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
        )
    }

    private fun observeModelContent(modelContent: Content) {
        //TODO set the Loading here
        when (modelContent) {
            is Content.EncodeImage -> {
                imageViewPreview.loadFile(File(modelContent.absolutePath))
                viewModel.onRequestCategoriesMessages(modelContent.absolutePath.encodeImage())
            }
            is Content.RequestModelContent -> navigateToCategories(modelContent.messages)
        }
    }


    private fun setPermissionView() {
        layoutCamera.gone()
        layoutPermission.visible()
        textViewEnableAccess.setOnClickListener {
            viewModel.onRequestCameraViewDisplay()
        }

    }


    private fun setPreviewView(previewModel: Event<PreviewModel>) {
        previewModel.getContentIfNotHandled()?.let {
            layoutCamera.gone()
            layoutPermission.gone()
            layoutPreview.visible()
            cameraUiContainer.gone()

            cancelButton.setOnClickListener {
                viewModel.onCancelRequest()
            }

        }
    }

    private fun navigateToCategories(content: Event<List<Message>>) {
        content.getContentIfNotHandled()?.let { messages ->
            mFragmentNavigation.pushFragment(CategoriesFragment.newInstance(messages[0]))
        }
    }


    private fun setCameraSpecs() {
        layoutCamera.visible()
        layoutPermission.gone()

        outputDirectory = CameraActivity.getOutputDirectory(requireContext())

        viewFinder.post {

            displayId = viewFinder.display.displayId

            initCameraUi()

            bindCameraUseCases(flashMode)

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
                }, ANIMATION_SLOW_MILLIS)

            }

        }


        flashSwitchButton.setOnClickListener {
            flashMode = if (ImageCapture.FLASH_MODE_ON == flashMode) {
                ImageCapture.FLASH_MODE_OFF
            } else {
                ImageCapture.FLASH_MODE_ON
            }
            bindCameraUseCases(flashMode)
        }

    }

}
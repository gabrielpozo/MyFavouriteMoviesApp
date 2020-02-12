package com.accenture.signify.ui.camera

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.accenture.signify.R
import com.accenture.signify.extensions.ANIMATION_FAST_MILLIS
import com.accenture.signify.extensions.ANIMATION_SLOW_MILLIS
import com.accenture.signify.extensions.loadFile
import com.accenture.signify.extensions.simulateClick
import com.accenture.util.KEY_EVENT_ACTION
import com.accenture.util.KEY_EVENT_EXTRA
import kotlinx.android.synthetic.main.camera_ui_container.*
import kotlinx.android.synthetic.main.camera_ui_container.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class CameraFragment : Fragment() {

    companion object {
        private const val TAG = "CameraX"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private var flashMode = ImageCapture.FLASH_MODE_OFF

        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
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

    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val shutter = container
                        .findViewById<ImageButton>(R.id.cameraCaptureButton)
                    shutter.simulateClick()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainExecutor = ContextCompat.getMainExecutor(requireContext())
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                CameraFragmentDirections.actionCameraToPermissions()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        broadcastManager.unregisterReceiver(volumeDownReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_camera, container, false)


    private fun setGalleryThumbnail(file: File) {

        val thumbnail = photoPreviewButton
        thumbnail.post {

            thumbnail.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

            thumbnail.loadFile(file)

        }
    }

    private val imageSavedListener = object : ImageCapture.OnImageSavedCallback {
        override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
            Timber.e("$TAG Photo capture failed: $message", cause)
        }

        @SuppressLint("ObsoleteSdkInt")
        override fun onImageSaved(photoFile: File) {
            Timber.d("$TAG Photo capture succeeded: ${photoFile.absolutePath}")

            setGalleryThumbnail(photoFile)

            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(photoFile.extension)
            MediaScannerConnection.scanFile(
                context, arrayOf(photoFile.absolutePath), arrayOf(mimeType), null
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        outputDirectory = CameraActivity.getOutputDirectory(requireContext())

        viewFinder.post {

            displayId = viewFinder.display.displayId

            initCameraUi()

            bindCameraUseCases(flashMode)

            lifecycleScope.launch(Dispatchers.IO) {
                outputDirectory.listFiles { file ->
                    EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
                }?.max()?.let {
                    setGalleryThumbnail(it)
                }
            }
        }
    }


    private fun bindCameraUseCases(flashMode: Int) {

        if (flashMode == ImageCapture.FLASH_MODE_OFF) {
            flashSwitchButton.setImageResource(R.drawable.flash_off)
        } else {
            flashSwitchButton.setImageResource(R.drawable.flash_on)
        }

        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Timber.d("$TAG, Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = setAspectRatio(metrics.widthPixels, metrics.heightPixels)
        Timber.d("$TAG Preview aspect ratio: $screenAspectRatio")

        val rotation = viewFinder.display.rotation

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)

                .setTargetRotation(rotation)
                .build()

            preview?.previewSurfaceProvider = viewFinder.previewSurfaceProvider

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setFlashMode(flashMode)
                .setTargetRotation(rotation)
                .build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
                .also {
                    it.setAnalyzer(mainExecutor, LuminosityAnalyzer { luma ->
                        Timber.d("$TAG Average luminosity: $luma")
                    })
                }

            cameraProvider.unbindAll()

            try {
                camera = cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Timber.e("$TAG Use case binding failed", exc)
            }

        }, mainExecutor)
    }


    private fun setAspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }


    private fun initCameraUi() {

        container.findViewById<ConstraintLayout>(R.id.cameraUiContainer)?.let {
            container.removeView(it)
        }

        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)


        controls.cameraCaptureButton.setOnClickListener {
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



        controls.photoPreviewButton.setOnClickListener {
            if (true == outputDirectory.listFiles()?.isNotEmpty()) {
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                    CameraFragmentDirections.actionCameraToGallery(outputDirectory.absolutePath)
                )
            }
        }
    }

}
package com.light.finder.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.DisplayMetrics
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.light.finder.R
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.camera.LuminosityAnalyzer
import kotlinx.android.synthetic.main.camera_ui_container.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


val EXTENSION_WHITELIST = arrayOf("JPG")

/*fun setAspectRatio(width: Int, height: Int): Int {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}*/


fun CameraFragment.bindCameraUseCases(flashMode: Int) {
    if (flashMode == ImageCapture.FLASH_MODE_OFF) {
        flashSwitchButton.setImageResource(R.drawable.ic_flash_off)
    } else {
        flashSwitchButton.setImageResource(R.drawable.ic_flash_on)
    }

    val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
    Timber.d("${CameraFragment.TAG}, Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

   /* val screenAspectRatio = setAspectRatio(
        metrics.widthPixels,
        metrics.heightPixels
    )*/


    val rotation = viewFinder.display.rotation

    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())



    cameraProviderFuture.addListener(Runnable {

        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        preview = Preview.Builder()
           // .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        preview?.previewSurfaceProvider = viewFinder.previewSurfaceProvider

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
                        Timber.d("${CameraFragment.TAG} Average luminosity: $luma")
                    })
            }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
            )
        } catch (exc: Exception) {
            Timber.e("${CameraFragment.TAG} Use case binding failed exc")
        }

    }, mainExecutor)

}


fun CameraFragment.checkSelfCameraPermission(): Boolean  = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED




fun String.encodeImage(): String {
    val bytes = File(this).readBytes()
    return resizeBase64Image(Base64.encodeToString(bytes, 0))
}


private fun resizeBase64Image(base64image: String): String {
    val encodeByte = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
    val options = BitmapFactory.Options()
    var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)


    if (image.height <= 600 && image.width <= 800) {
        return base64image
    }
    image = Bitmap.createScaledBitmap(image, 800, 600, false)

    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

    val b = baos.toByteArray()
    System.gc()
    return Base64.encodeToString(b, Base64.NO_WRAP)

}

private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0

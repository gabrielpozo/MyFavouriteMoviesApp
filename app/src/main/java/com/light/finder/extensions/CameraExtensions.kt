package com.light.finder.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Base64
import androidx.core.content.ContextCompat
import com.light.finder.ui.camera.CameraFragment
import java.io.File


val EXTENSION_WHITELIST = arrayOf("JPG")

fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun CameraFragment.checkSelfCameraPermission(): Boolean = ContextCompat.checkSelfPermission(
    requireContext(),
    Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED

fun CameraFragment.checkSelfStoragePermission(): Boolean = ContextCompat.checkSelfPermission(
    requireContext(),
    Manifest.permission.READ_EXTERNAL_STORAGE
) == PackageManager.PERMISSION_GRANTED


fun String.encodeImage(): String {
    val bytes = File(this).readBytes()
    return Base64.encodeToString(bytes, 0)

}


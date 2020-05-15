package com.light.finder.extensions

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.util.Base64
import androidx.core.content.ContextCompat
import com.light.finder.ui.camera.CameraFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer


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


fun String.encodeImage(): String {
    val bytes = File(this).readBytes()
    return resizeBase64Image(Base64.encodeToString(bytes, 0))
}

//todo change this to efficient way
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


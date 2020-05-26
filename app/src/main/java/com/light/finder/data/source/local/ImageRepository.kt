package com.light.finder.data.source.local

import android.graphics.Bitmap
import android.media.Image
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.light.finder.extensions.toBitmap
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext


class ImageRepository(private val uiDispatcher: CoroutineDispatcher) : CoroutineScope {

    private var job: Job = SupervisorJob()

    companion object {
        const val bitmapWidth = 800
        const val bitmapHeight = 600
    }

    override val coroutineContext: CoroutineContext
        get() = uiDispatcher + job

    fun getBitmap(image: Image): Bitmap {
        return image.toBitmap()
    }

    fun convertImageToBase64(bitmap: Bitmap, base64: (String) -> Unit) {
        launch {
            base64.invoke(toBase64(bitmap))
        }
    }

    private suspend fun toBase64(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        if (bitmap.height <= bitmapHeight && bitmap.width <= bitmapWidth) {
            encodeBitmapTo64(bitmap)
        } else {
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false)
            System.gc()
            encodeBitmapTo64(Bitmap.createScaledBitmap(scaledBitmap, bitmapWidth, bitmapHeight, false))
        }
    }

    private fun encodeBitmapTo64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
       return encodeToString(b, DEFAULT)
    }
}

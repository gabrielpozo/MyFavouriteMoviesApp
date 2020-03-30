package com.light.finder.data.source.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext


class ImageRepository(private val uiDispatcher: CoroutineDispatcher) : CoroutineScope {

    private var job: Job = SupervisorJob()


    override val coroutineContext: CoroutineContext
        get() = uiDispatcher + job

    fun getBitmap(image: Image): Bitmap {
        return decodeBitmap(image)
    }

    fun convertImageToBase64(bitmap: Bitmap, base64: (String) -> Unit) {
        launch {
            base64.invoke(toBase64(bitmap))
        }
    }

    private suspend fun toBase64(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
        val base64 = resizeBase64Image(encodeToString(b, DEFAULT))
        base64
    }


    private fun decodeBitmap(image: Image): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity()).also { buffer.get(it) }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
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
        return encodeToString(b, Base64.NO_WRAP)

    }
}

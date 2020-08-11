package com.light.finder.data.source.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.light.finder.common.exceptions.BitmapDecodeException
import com.light.finder.extensions.toBitmap
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.coroutines.CoroutineContext


class ImageRepository(private val uiDispatcher: CoroutineDispatcher) : CoroutineScope {

    private var job: Job = SupervisorJob()

    companion object {
        const val bitmapWidth = 800
        const val bitmapHeight = 600
        const val DECODE_BITMAP_EXCEPTION_MESSAGE = "DECODE_BITMAP_EXCEPTION_MESSAGE"
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
            encodeBitmapTo64(
                Bitmap.createScaledBitmap(
                    scaledBitmap,
                    bitmapWidth,
                    bitmapHeight,
                    false
                )
            )
        }
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromStream(
        inputStream: InputStream
    ): Bitmap {
        var byteArr = ByteArray(0)
        val buffer = ByteArray(1024)
        var len: Int
        var count = 0
        return try {
            while (inputStream.read(buffer).also { len = it } > -1) {
                if (len != 0) {
                    if (count + len > byteArr.size) {
                        val newbuf = ByteArray((count + len) * 2)
                        System.arraycopy(byteArr, 0, newbuf, 0, count)
                        byteArr = newbuf
                    }
                    System.arraycopy(buffer, 0, byteArr, count, len)
                    count += len
                }
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(byteArr, 0, count, options)
            options.inSampleSize = calculateInSampleSize(
                options,
                bitmapWidth,
                bitmapHeight
            )
            options.inPurgeable = true
            options.inInputShareable = true
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeByteArray(byteArr, 0, count, options)
        } catch (e: java.lang.Exception) {
            //TODO("implement a solution to let the user know(e.g snackbar or popup) that something went terribly wrong in his device")
            throw BitmapDecodeException(DECODE_BITMAP_EXCEPTION_MESSAGE)
        }
    }

    private fun encodeBitmapTo64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
        return encodeToString(b, DEFAULT)
    }
}

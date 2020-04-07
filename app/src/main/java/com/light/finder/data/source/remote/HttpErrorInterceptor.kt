package com.light.finder.data.source.remote

import com.crashlytics.android.Crashlytics
import com.light.util.FAILED_RECOGNITION
import com.light.util.FORMAT_INVALID
import com.light.util.SERVER_ERROR_RECOGNITION
import com.light.util.TIMEOUT_ERROR_RECOGNITION
import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val timeSent = response.sentRequestAtMillis
        val timeReceived = response.receivedResponseAtMillis
        val isTimeout = timeReceived - timeSent > 5000L
        var code = response.code

        if (isTimeout) {
            // 604 custom timeout error
            code = 604
        }

        when (response.code != 200) {
           true -> Crashlytics.logException(CrashlyticsException(code))
        }

        return response
    }
}

class CrashlyticsException(var tag: Int) : Exception() {

    private var msg: String? = "Crashlytics Exception"

    init {
        when (tag) {
            204 -> msg = FAILED_RECOGNITION
            422 -> msg = FORMAT_INVALID
            500 -> msg = SERVER_ERROR_RECOGNITION
            604 -> msg = TIMEOUT_ERROR_RECOGNITION
        }

        //val e = Exception(msg)
        Crashlytics.setInt("Response Code", tag)
        Crashlytics.log(msg)
    }

}
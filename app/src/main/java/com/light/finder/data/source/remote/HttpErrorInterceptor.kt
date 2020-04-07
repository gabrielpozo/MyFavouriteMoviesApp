package com.light.finder.data.source.remote

import com.crashlytics.android.Crashlytics
import okhttp3.Interceptor
import okhttp3.Request
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
            204 -> msg = "Image Recognition failed to recognise"
            422 -> msg = "Image Recognition format not valid"
            500 -> msg = "Image Recognition server error"
            604 -> msg = "Image Recognition timeout error"
        }

        //val e = Exception(msg)
        Crashlytics.setInt("Response Code", tag)
        Crashlytics.log(msg)
    }

}
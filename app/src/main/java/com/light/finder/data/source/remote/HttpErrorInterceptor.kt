package com.light.finder.data.source.remote

import android.util.Log
import com.crashlytics.android.Crashlytics
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpErrorInterceptor : Interceptor {
    companion object {
        //val httpErrorCodes = arrayOf(204, 401, 404, 405, 500)
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        val tx = response.sentRequestAtMillis
        val rx = response.receivedResponseAtMillis
        val isTimeout = rx - tx > 5000L

        if (isTimeout) {
            Crashlytics.logException(CrashlyticsException(604))
        }

        when (response.code) {
            204 -> Crashlytics.logException(CrashlyticsException(response.code))
            422 -> Crashlytics.logException(CrashlyticsException(response.code))
            500 -> Crashlytics.logException(CrashlyticsException(response.code))
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
            // 604 custom timeout error
            604 -> msg = "Image Recognition timeout error"
        }

        //val e = Exception(msg)
        Crashlytics.setInt("Response Code", tag)
        Crashlytics.log(msg)
    }

}
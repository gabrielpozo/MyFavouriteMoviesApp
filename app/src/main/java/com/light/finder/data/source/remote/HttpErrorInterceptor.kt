package com.light.finder.data.source.remote

import com.crashlytics.android.Crashlytics
import com.light.finder.data.source.remote.reports.CrashlyticsException
import com.light.util.FAILED_RECOGNITION
import com.light.util.FORMAT_INVALID
import com.light.util.SERVER_ERROR_RECOGNITION
import com.light.util.TIMEOUT_ERROR_RECOGNITION
import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code != 200) CrashlyticsException(response.code).logException()
        return response
    }
}


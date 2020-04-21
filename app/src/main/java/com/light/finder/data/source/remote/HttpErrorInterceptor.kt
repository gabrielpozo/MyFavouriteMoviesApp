package com.light.finder.data.source.remote

import com.light.finder.data.source.remote.reports.CrashlyticsException
import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code != 200) CrashlyticsException(response.code, null, null).logException()
        return response
    }
}


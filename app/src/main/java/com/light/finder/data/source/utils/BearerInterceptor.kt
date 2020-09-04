package com.light.finder.data.source.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

//TODO Akis put this in MessageRemoteUtil Okhttp as interceptor
class BearerInterceptor(private var authToken: String?) : Interceptor {
    fun setAuthToken(authToken: String?) {
        this.authToken = authToken
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "Bearer $authToken")
        //TODO Akis change bearer token with the saved one from oauth call
        return chain.proceed(builder.build())
    }
}


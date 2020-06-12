package com.light.finder.data.source.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "Bearer 49385fe86a8fd424a98bacbdd8845357")
        return chain.proceed(builder.build())
    }
}
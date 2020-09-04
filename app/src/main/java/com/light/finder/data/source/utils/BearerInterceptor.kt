package com.light.finder.data.source.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

//TODO Akis put this in MessageRemoteUtil Okhttp as interceptor
class BearerInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "Bearer 49385fe86a8fd424a98bacbdd8845357")
        //TODO Akis change bearer token with the saved one from oauth call
        return chain.proceed(builder.build())
    }
}
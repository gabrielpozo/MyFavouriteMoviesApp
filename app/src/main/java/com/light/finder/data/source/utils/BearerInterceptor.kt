package com.light.finder.data.source.utils

import okhttp3.Interceptor

class BearerInterceptor(private val tokenType: String = "Bearer", private val accessToken: String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $accessToken").build()

        return chain.proceed(request)
    }
}

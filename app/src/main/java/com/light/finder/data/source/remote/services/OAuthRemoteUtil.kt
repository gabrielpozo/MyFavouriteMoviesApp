package com.light.finder.data.source.remote.services

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.common.HiddenAnnotationExclusionStrategy
import com.light.finder.data.source.utils.HttpErrorInterceptor
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OAuthRemoteUtil {

    private const val CLIENT_ID = "4pqsud54rr141gghtmgipmj257"
    private const val CLIENT_SECRET = "h6jpssq6cpsgamngaiag29644ae19uc682a6d943j2ov3aa2t5s"

    var tokenInterceptor: Interceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .header("Authorization", Credentials.basic(CLIENT_ID, CLIENT_SECRET))
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(tokenInterceptor)
        addInterceptor(HttpErrorInterceptor())
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()


    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient().setExclusionStrategies(
        HiddenAnnotationExclusionStrategy()
    )

    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.OAUTH_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(
                SignifyApiService::class.java
            )
        }
}
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

    private const val CLIENT_ID = BuildConfig.CLIENT_ID
    private const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET

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
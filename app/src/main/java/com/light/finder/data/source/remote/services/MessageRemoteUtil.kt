package com.light.finder.data.source.remote.services

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.common.HiddenAnnotationExclusionStrategy
import com.light.finder.data.source.utils.HttpErrorInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MessageRemoteUtil {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpErrorInterceptor())
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()


    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient().setExclusionStrategies(
        HiddenAnnotationExclusionStrategy()
    )

    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(
                SignifyApiService::class.java)
        }
}
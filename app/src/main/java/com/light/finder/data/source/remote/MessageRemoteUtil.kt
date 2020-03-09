package com.light.finder.data.source.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MessageRemoteUtil {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient()


    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(SignifyApiService::class.java)
        }
}
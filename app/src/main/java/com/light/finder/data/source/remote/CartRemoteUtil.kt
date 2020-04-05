package com.light.finder.data.source.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookiePolicy.ACCEPT_ALL



object CartRemoteUtil {

    val cookieJar = JavaNetCookieJar(CookieManager())

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient()


    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.CART_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(SignifyApiService::class.java)
        }
}
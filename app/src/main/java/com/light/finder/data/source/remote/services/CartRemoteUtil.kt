package com.light.finder.data.source.remote.services

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.data.source.utils.WebKitSyncCookieManager
import com.light.finder.extensions.createCookieStore
import com.light.util.SingletonHolder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookiePolicy


class CartRemoteUtil private constructor(val context: Context) {

    private val cookieManager =
        WebKitSyncCookieManager(
            context.createCookieStore(name = "CartCookies", persistent = true),
            CookiePolicy.ACCEPT_ALL
        )

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        cookieJar(JavaNetCookieJar(cookieManager))
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()

    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient()


    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.CART_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(
                SignifyApiService::class.java
            )
        }

    companion object : SingletonHolder<CartRemoteUtil, Context>(::CartRemoteUtil)
}

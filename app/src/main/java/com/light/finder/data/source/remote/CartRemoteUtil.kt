package com.light.finder.data.source.remote

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.extensions.createCookieStore
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookiePolicy


class CartRemoteUtil private constructor(val context: Context) {

    private val cookieManager = WebKitSyncCookieManager(
        context.createCookieStore(name = "CartCookies", persistent = true),
        CookiePolicy.ACCEPT_ALL
    )

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        cookieJar(JavaNetCookieJar(cookieManager))
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.also {
        if (BuildConfig.DEBUG) {
            it.addNetworkInterceptor(AddHeaderInterceptor())
        }
    }.build()

    val gsonBuilder: GsonBuilder = GsonBuilder().setLenient()


    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.CART_URL_QA else BuildConfig.CART_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()
        .run {
            create<SignifyApiService>(SignifyApiService::class.java)
        }

    companion object : SingletonHolder<CartRemoteUtil, Context>(::CartRemoteUtil)
}

open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}
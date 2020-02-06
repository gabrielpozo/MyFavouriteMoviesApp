package com.accenture.signify.data.source.remote

import com.accenture.signify.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

object MessageRemoteUtil {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .addInterceptor(HttpLoggingInterceptor())
        .build()


    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .run {
            create<SignifyApiService>(SignifyApiService::class.java)
        }
}
package com.light.finder.data.source.remote.services

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.SignifyApp
import com.light.finder.common.HiddenAnnotationExclusionStrategy
import com.light.finder.data.source.local.LocalKeyStoreImpl
import com.light.finder.data.source.utils.BearerInterceptor
import com.light.finder.data.source.utils.HttpErrorInterceptor
import com.light.finder.data.source.utils.TokenAuthenticator
import com.light.source.local.LocalKeyStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MessageRemoteUtil {

    private val localKeyStore: LocalKeyStore by lazy {
        LocalKeyStoreImpl(
            SignifyApp.getContext()!!
        )
    }


    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            BearerInterceptor(
                tokenType = localKeyStore.loadBearerToken()?.tokenType ?: "",
                accessToken = localKeyStore.loadBearerToken()?.accessToken ?: ""
            )
        )
        addInterceptor(HttpErrorInterceptor())
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        authenticator(TokenAuthenticator(localKeyStore))
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
                SignifyApiService::class.java
            )
        }
}
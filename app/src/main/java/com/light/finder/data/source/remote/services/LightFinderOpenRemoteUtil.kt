package com.light.finder.data.source.remote.services

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.light.finder.BuildConfig
import com.light.finder.SignifyApp
import com.light.finder.data.source.local.LocalKeyStoreImpl
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.utils.BearerInterceptor
import com.light.finder.data.source.utils.HttpErrorInterceptor
import com.light.finder.data.source.utils.TokenAuthenticator
import com.light.source.local.LocalKeyStore
import com.light.source.local.LocalPreferenceDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object LightFinderOpenRemoteUtil {

    private val localKeyStore: LocalKeyStore by lazy {
        LocalKeyStoreImpl(
            SignifyApp.getContext()!!
        )
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            BearerInterceptor(
                tokenType = localKeyStore.loadBearerToken().tokenType,
                accessToken = localKeyStore.loadBearerToken().accessToken
            )
        )
        addInterceptor(HttpErrorInterceptor())
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        authenticator(TokenAuthenticator(localKeyStore))
    }.build()


    private val gsonBuilder: GsonBuilder = GsonBuilder().setLenient()

    val service: SignifyApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.LIGHT_FINDER_OPEN_URL)
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
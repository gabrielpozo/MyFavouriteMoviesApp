package com.gabriel.myfavouritemoviesapp.data.source.remote

import com.gabriel.myfavouritemoviesapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object TheMovieDb {
    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    val service: TheMovieDbService = Retrofit.Builder()
        .baseUrl(BuildConfig.movie_db_api_key)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .run { create(TheMovieDbService::class.java) }
}
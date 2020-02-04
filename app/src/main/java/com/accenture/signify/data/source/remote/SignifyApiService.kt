package com.accenture.signify.data.source.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface SignifyApiService {

    @GET("/")
    fun fetchMessageAsync(): Deferred<CategoryResultDto>
}
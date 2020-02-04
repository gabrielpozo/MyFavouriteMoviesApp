package com.accenture.signify.data.source.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface SignifyApiService {

    //todo move it to constants
    companion object {
        const val BASE_URL = "https://sajy0j9z0l.execute-api.us-east-2.amazonaws.com/v1/api"
    }

    @GET("/")
    fun fetchMessage(): Deferred<CategoryResultDto>
}
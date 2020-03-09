package com.light.finder.data.source.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface SignifyApiService {

    @Headers("Content-Type: application/json")
    @POST("lamp64")
    suspend fun fetchMessageAsync(
        @Body body: Image
    ): Response<CategoryResultDto>
}


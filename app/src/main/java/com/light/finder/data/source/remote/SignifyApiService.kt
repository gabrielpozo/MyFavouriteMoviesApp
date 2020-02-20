package com.light.finder.data.source.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface SignifyApiService {

    @Headers("Content-Type: application/json")
    @POST("lamp64")
    fun fetchMessageAsync(
        @Body body: Image
    ): Deferred<CategoryResultDto>
}


package com.accenture.signify.data.source.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface SignifyApiService {

    @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("lamp64")
    suspend fun fetchMessageAsync(
        @FieldMap params: HashMap<String, String>
    ): Deferred<CategoryResultDto>
}


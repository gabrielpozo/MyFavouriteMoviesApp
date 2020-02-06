package com.accenture.signify.data.source.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface SignifyApiService {

    @POST("lamp64/")
    suspend fun fetchMessageAsync(
        @Header("Content-Type") contentType: String, @Body body: HashMap<String, String>
    ): Deferred<CategoryResultDto>
}


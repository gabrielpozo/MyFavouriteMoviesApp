package com.light.finder.data.source.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface SignifyApiService {

    @Headers("Content-Type: application/json")
    @POST("lamp64")
    suspend fun fetchMessageAsync(
        @Body body: Image
    ): Response<CategoryResultDto>

    @Headers("Content-Type: application/json")
    @POST("addToCart/sku/{product_SAPid_12NC}")
    suspend fun fetchCartAsync(
        @Path("product_SAPid_12NC") productSapId: String
    ): Response<CartResultDto>

    @Headers("Content-Type: application/json")
    @POST("getCart")
    suspend fun fetchCartItemCountAsync(
    ): Response<CartItemCountResultDto>
}


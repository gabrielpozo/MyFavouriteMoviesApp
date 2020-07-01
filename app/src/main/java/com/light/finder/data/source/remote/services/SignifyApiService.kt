package com.light.finder.data.source.remote.services

import com.light.finder.data.source.remote.CartItemCountResultDto
import com.light.finder.data.source.remote.dto.CartResultDto
import com.light.finder.data.source.remote.dto.CategoryResultDto
import com.light.finder.data.source.remote.Image
import com.light.finder.data.source.remote.dto.LegendDto
import com.light.finder.data.source.remote.dto.LegendParsingDto
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

    @Headers("Content-Type: application/json")
    @POST("legend")
    suspend fun fetchLegendTagsAsync(
    ): Response<LegendParsingDto>
}


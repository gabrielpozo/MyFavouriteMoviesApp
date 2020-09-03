package com.light.finder.data.source.remote.services

import com.light.domain.model.Credential
import com.light.finder.data.source.remote.CartItemCountResultDto
import com.light.finder.data.source.remote.Image
import com.light.finder.data.source.remote.dto.*
import retrofit2.Response
import retrofit2.http.*


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
    @GET("legend")
    suspend fun fetchLegendTagsAsync(
    ): Response<LegendParsingDto>

    @Headers("Content-Type: application/json")
    @GET("browse")
    suspend fun fetchBrowsingProductsAsync(
    ): Response<ProductBrowsingListDto>

    //todo headers?
    @Headers("Content-Type: application/json")
    @POST("token")
    @FormUrlEncoded
    suspend fun fetchBearerTokenAsync(@Body credential: Credential): Response<BearerResultDto>
}


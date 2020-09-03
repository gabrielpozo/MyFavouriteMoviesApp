package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class BearerResultDto(
    @SerializedName("access_token") val error: String?,
    @SerializedName("expires_in") val success: Long?,
    @SerializedName("token_type") val product: CartProductDto?
)
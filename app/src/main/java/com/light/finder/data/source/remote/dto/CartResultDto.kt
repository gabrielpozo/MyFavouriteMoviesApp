package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName


data class CartResultDto(
    @SerializedName("Error") val error: String?,
    @SerializedName("Success") val success: String?,
    @SerializedName("product") val product: CartProductDto?
)

data class CartProductDto(
    @SerializedName("name") val name: String?
)
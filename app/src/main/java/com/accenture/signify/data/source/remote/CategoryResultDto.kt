package com.accenture.signify.data.source.remote

import com.google.gson.annotations.SerializedName


data class CategoryResultDto(@SerializedName("message") val messageList: List<MessageDto>)

data class MessageDto(
    @SerializedName("categories")
    val categories: List<CategoriesDto>,
    @SerializedName("version")
    val version: String,
    @SerializedName("formfactor")
    val formFactor: String
)

data class CategoriesDto(
    @SerializedName("category_products") val categoryProducts: List<String>,
    @SerializedName("category_energysave") val categoryEnergysave: String,
    @SerializedName("category_index") val categoryIndex: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_image") val categoryImage: String,
    @SerializedName("category_price") val categoryPrice: String
)


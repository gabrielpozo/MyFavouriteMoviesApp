package com.accenture.signify.data.source.remote

import com.google.gson.annotations.SerializedName


data class CategoryResultDto(@SerializedName("message") val messageList: List<MessageDto>)

data class MessageDto(
    @SerializedName("categories")
    val categories: List<CategoriesDto>
)

data class CategoriesDto(
    @SerializedName("category_products") val categoryProducts: List<ProductDto>,
    @SerializedName("category_energysave") val categoryEnergysave: String,
    @SerializedName("category_index") val categoryIndex: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_image") val categoryImage: String,
    @SerializedName("category_price") val categoryPrice: String
)


data class ProductDto(
    @SerializedName("product_image") val productImage: List<String>,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_description") val productDescription: String,
    @SerializedName("product_spec1") val productSpecOne: String,
    @SerializedName("product_spec3") val productSpecThree: String,
    @SerializedName("product_scene") val productScene: String

)


package com.light.finder.data.source.remote

import com.google.gson.annotations.SerializedName


data class CategoryResultDto(@SerializedName("message") val messageList: List<MessageDto>? = null)

data class MessageDto(
    @SerializedName("categories")
    val categories: List<CategoriesDto>?
)

data class CategoriesDto(
    @SerializedName("category_filter_product_base") val categoryProductBase: String?,
    @SerializedName("category_products") val categoryProducts: List<ProductDto>?,
    @SerializedName("category_index") val categoryIndex: Int?,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("category_image") val categoryImage: String?,
    @SerializedName("category_price") val categoryPrice: Price?,
    @SerializedName("category_filter_watt_replaced") val categoryWattReplace: List<Int>?

)


data class ProductDto(
    @SerializedName("product_image") val productImage: List<String>?,
    @SerializedName("product_category_name") val productCategoryName: String?,
    @SerializedName("product_name") val productName: String?,
    @SerializedName("product_description") val productDescription: String?,
    @SerializedName("product_spec1") val productSpecOne: Float?,
    @SerializedName("product_spec3") val productSpecThree: List<String>?,
    @SerializedName("product_scene") val productScene: String?,
    @SerializedName("product_price_lamp") val productPrice: Float?
)


data class Price(
    @SerializedName("max_price") val maxPrice: Float?,
    @SerializedName("min_price") val minPrice: Float?
)



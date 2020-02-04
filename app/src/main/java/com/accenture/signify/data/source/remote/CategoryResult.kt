package com.accenture.signify.data.source.remote

import com.google.gson.annotations.SerializedName


data class CategoryResult(@SerializedName("message") val message: List<Message>)

data class Categories(
    @SerializedName("category_products") val categoryProducts: List<String>,
    @SerializedName("category_energysave") val categoryEnergysave: String,
    @SerializedName("category_index") val categoryIndex: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_image") val categoryImage: String,
    @SerializedName("category_price") val categoryPrice: String
)

data class Message(
    @SerializedName("categories")
    val categories: List<Categories>,
    @SerializedName("version")
    val version: String,
    @SerializedName("formfactor")
    val formFactor: String
)
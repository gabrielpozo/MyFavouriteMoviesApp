package com.accenture.signify.data.source.remote

import com.google.gson.annotations.SerializedName


data class CategoryResultDto(@SerializedName("message") val messageList: MessageDto)

data class MessageDto(
    @SerializedName("categories")
    val categories: List<CategoriesDto>
)

data class CategoriesDto(
    @SerializedName("category_name") val categoryName: String
)


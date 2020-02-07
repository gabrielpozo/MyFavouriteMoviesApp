package com.accenture.domain.model

data class Message(
    val categories: List<Categories>)


data class Categories(
    val categoryName: String

)
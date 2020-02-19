package com.accenture.domain.model

data class Message(
    val categories: List<Category>
)


data class Category(
    val categoryProducts: List<Product>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String
)

data class Product(
    val productImage: List<String>,
    val productName: String,
    val productDescription: String,
    val productSpecOne: String,
    val productSpecThree: String,
    val productScene: String,
    var filtered: Boolean = false
)
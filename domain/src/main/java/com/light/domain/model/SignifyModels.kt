package com.light.domain.model

data class Message(
    val categories: List<Category>
)


data class Category(
    val categoryProductBase: String,
    val categoryProducts: List<Product>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val priceRange: String,
    val wattageAvailable: Int
)

data class Product(
    val productImage: List<String>,
    val productName: String,
    val productDescription: String,
    val productSpecOne: String,
    val productSpecThree: String,
    val productScene: String,
    val productPrice: String,
    var filtered: Boolean = false

)
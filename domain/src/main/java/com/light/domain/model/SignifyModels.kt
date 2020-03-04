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
    val productCategoryName: String,
    val productName: String,
    val productDescription: String,
    val productSpecOne: Float,
    val productSpecThree: List<String>,
    val productScene: String,
    val productPrice: Float,
    var filtered: Boolean = false
)
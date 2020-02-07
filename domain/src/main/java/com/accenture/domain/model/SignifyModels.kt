package com.accenture.domain.model

data class Message(
    val categories: List<Categories>
)


data class Categories(
    val categoryProducts: List<Product>,
    val categoryEnergySave: String,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val categoryPrice: String
)

data class Product(
    val productImage: List<String>,
    val productName: String,
    val productDescription: String,
    val productSpecOne: String,
    val productSpecThree: String,
    val productScene: String
)
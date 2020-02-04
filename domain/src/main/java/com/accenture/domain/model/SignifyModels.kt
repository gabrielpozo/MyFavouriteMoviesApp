package com.accenture.domain.model

data class Message(
    val categories: List<Categories>,
    val version: String,
    val formFactor: String
)


data class Categories(
    val categoryProducts: List<String>,
    val categoryEnergySave: String,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val categoryPrice: String
)
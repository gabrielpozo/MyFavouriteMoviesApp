package com.light.domain.model

data class Cart(
    val success : String,
    val error : String,
    val product : CartProduct
)


data class CartProduct(
    val name: String
)
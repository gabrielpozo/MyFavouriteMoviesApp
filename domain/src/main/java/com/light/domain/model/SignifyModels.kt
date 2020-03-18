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
    val minWattage: String,
    val maxWattage: String,
    val colors: List<String>
)

data class Product(
    var name: String,
    var index: Int,
    var spec1: Float,
    var spec3: List<Int>,
    var spec2: String,
    var imageUrls: List<String>,
    var description: String,
    var scene: String,
    var categoryName: String,
    var sapID12NC: Long,
    var qtyLampscase: Int,
    var wattageReplaced: Int,
    var country: String,
    var priority: Int,
    var wattageClaim: Float,
    var factorBase: String,
    var discountProc: Int,
    var sapID10NC: Long,
    var dimmingCode: Int,
    var finish: String,
    var promoted: Int,
    var priceSku: Float,
    var priceLamp: Float,
    var pricePack: Float,
    var factorShape: String,
    var qtyLampSku: Int,
    var discountValue: Int,
    var qtySkuCase: Int,
    var factorTypeCode: Int,
    var colorCctCode: String,
    var filtered: Boolean = false
)
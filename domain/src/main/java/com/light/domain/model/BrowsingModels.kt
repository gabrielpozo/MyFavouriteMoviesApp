package com.light.domain.model


data class ProductBrowsing(
    val productCountry: String,
    val productPromoted: Int,
    val productDiscountProc: Int,
    val productDiscountValue: Int,
    val productFormfactorBase: String,
    val productFormfactorShape: String,
    val productFinishCode: Int,
    val productCctCode: Int,
    val productDimming_code: Int,
    val productFinish: String,
    val productName: String,
    val productPrio: Int,
    val productCategoryName: String,
    val productWattageClaim: Double,
    val productWattageReplaced: Int,
    val productWattageReplacedExtra: String,
    val productPricePack: Float,
    val productPriceSku: Float,
    val productPriceLamp: Float,
    val productSAPid10NC: Long,
    val productSAPid12NC: Long,
    val productQtyLampsku: Int,
    val productQtySkucase: Int,
    val productQtyLampscase: Int,
    val productDescription: String,
    val productFormfactorTypeCode: Int,
    val productFormfactorType: String,
    val productFormfactorShapeId: Int,
    val productFormfactorBaseId: Int,
    val productCategoryCode: Int,
    val energyConsumption: Int,
    val productConnectionCode: Int,
    val productScene: String,
    val productSceneImage: List<String>,
    val productSceneXoptions: List<String>,
    val productSceneYoptions: List<String>,
    val productDetailsIcons: List<String>,
    val productDetailsIconsImages: List<String>,
    val productIndex: Int,
    val productImage: List<String>,
    val productSpec1: Double,
    val productSpec2: String,
    val productSpec3: List<Int>,
    var isSelected: Boolean = false
)

data class Key(val productCategoryCode: Int, val productFormFactorShapeId: Int)
data class KeyChoice(val productCategoryCode: Int)


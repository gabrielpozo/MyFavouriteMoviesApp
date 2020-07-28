package com.light.domain.model

data class Message(
    val categories: List<Category>,
    val version: String,
    val baseIdentified: String,
    val formfactorType: String,
    val shapeIdentified: String
)

data class Category(
    val categoryProductBase: String,
    val categoryProducts: List<Product>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val priceRange: String,
    val categoryWattReplaced: List<Int>,
    val maxEnergySaving: Float,
    val minEnergySaving: Float,
    val colors: List<Int>,
    val finishCodes: List<Int>,
    val categoryShape: String
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
    var colorCctCode: Int,
    var formfactorType: Int,
    var productFinishCode: Int,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = false,
    var filtered: Boolean = false,
    var wattageReplacedExtra: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Product

        return other.colorCctCode == this.colorCctCode
                && this.productFinishCode == other.productFinishCode
                && this.wattageReplaced == other.wattageReplaced
    }

    override fun hashCode(): Int {
        return wattageReplaced * colorCctCode * productFinishCode
    }
}


data class LegendParsing(val legend: LegendValue)
data class LegendValue(
    val productFormFactorType: List<FormFactorType>,
    val finishType: List<FinishType>,
    val cctType: List<CctType>,
    val formfactorTypeId: List<FormFactorTypeId>
)

data class FormFactorType(
    val id: Int,
    val name: String,
    val image: String,
    val order: String
)

data class FinishType(
    val id: Int,
    val name: String,
    val image: String,
    val order: String
)

data class CctType(
    val id: Int,
    val name: String,
    val smallIcon: String,
    val bigIcon: String,
    val order: Int,
    val arType: Int,
    val kelvinSpec: KelvinSpec,
    var isSelected: Boolean = false
)

data class FormFactorTypeId(
    val id: Int,
    val name: String,
    val productFormFactorType: String,
    val productFormFactorTypeId: Int,
    val image: String?,
    val description: String,
    val order: Int
)

data class KelvinSpec(val minValue: Int, val maxValue: Int, val defaultValue: Int)
data class ColorOrderList(val cctCode: Int, val order: Int)
data class FinishOrderList(val finnishCode: Int, val order: Int)
package com.light.domain.model

data class Bearer(
    val accessToken: String,
    var expiresIn: Long,
    val tokenType: String
)

data class Message(
    val categories: List<Category>,
    val version: String,
    var baseIdentified: String,
    val formfactorType: String,
    val shapeIdentified: String,
    val textIdentified: String,
    val imageIdentified: String,
    var shapeNameList: List<String>? = emptyList()
)

data class Category(
    val categoryProductBase: String,
    val categoryProducts: List<Product>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val priceRange: String,
    val maxPrice: Float,
    val minPrice: Float,
    val categoryWattReplaced: List<Int>,
    val maxEnergySaving: Float,
    val minEnergySaving: Float,
    val colors: List<Int>,
    val finishCodes: List<Int>,
    val categoryShape: String,
    val categoryConnectivityCode: List<Int>,
    val categoryDescription: String,
    var order: Int = -1
)

data class Product(
    var name: String = "",
    var index: Int = 0,
    var spec1: Float = 0.0f,
    var spec3: List<Int> = emptyList(),
    var spec2: String = "",
    var imageUrls: List<String>,
    var description: String,
    var scene: String = "",
    var categoryName: String = "",
    var sapID12NC: Long,
    var qtyLampscase: Int,
    var wattageReplaced: Int,
    var country: String = "",
    var productPrio: Int = -1,
    var wattageClaim: Float = 0.0f,
    var factorBase: String,
    var discountProc: Int = -1,
    var sapID10NC: Long = 0,
    var dimmingCode: Int = 0,
    var finish: String = "",
    var promoted: Int = -1,
    var priceSku: Float,
    var priceLamp: Float,
    var pricePack: Float,
    var factorShape: String,
    var qtyLampSku: Int,
    var discountValue: Int = -1,
    var qtySkuCase: Int = -1,
    var factorTypeCode: Int,
    var colorCctCode: Int,
    var formfactorType: Int,
    var productFinishCode: Int,
    var productConnectionCode: Int,
    var produtCategoryCode: Int,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = false,
    var filtered: Boolean = false,
    var wattageReplacedExtra: String,
    var stickyHeaderFirstLine: String,
    var stickyHeaderSecondLine: String
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
    val formfactorTypeId: List<FormFactorTypeId>,
    val productConnectivity: List<ProductConnectivity>,
    val formfactorTypeBaseId: List<FormFactorTypeBaseId>,
    val productCategoryName: List<ProductCategoryName>
)

data class ProductConnectivity(
    val id: Int,
    val name: String,
    val image: String,
    val order: String
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

data class FormFactorTypeBaseId(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String,
    val order: Int,
    var isSelected: Boolean = false
)

data class ProductCategoryName(
    val id: Int,
    val name: String,
    val order: Int,
    val image: String?,
    val description: String
)


data class KelvinSpec(val minValue: Int, val maxValue: Int, val defaultValue: Int)
data class ColorOrderList(val cctCode: Int, val order: Int)
data class FinishOrderList(val finnishCode: Int, val order: Int)
data class CategoryOrderList(val category: Category, val order: Int)


/**
 * Browsing
 */
data class ShapeBrowsing(
    val id: Int,
    val name: String,
    val image: String?,
    val order: Int,
    val subtitleCount: Int,
    var baseIdFitting: Int = -1,
    var baseNameFitting: String = "",
    var isSelected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as ShapeBrowsing

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}


data class ChoiceBrowsing(
    val id: Int,
    val name: String,
    val order: Int,
    val image: String?,
    val subtitleCount: Int,
    var description: String = "",
    var baseIdFitting: Int = -1,
    var baseNameFitting: String = "",
    var isSelected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as ChoiceBrowsing

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return id
    }
}

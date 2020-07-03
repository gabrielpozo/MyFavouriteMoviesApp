package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName


data class CategoryResultDto(@SerializedName("message") val messageList: List<MessageDto>? = null)

data class MessageDto(
    @SerializedName("categories")
    val categories: List<CategoriesDto>?,
    @SerializedName("version")
    val version: String,
    @SerializedName("base_identified")
    val baseIdentified: String,
    @SerializedName("formfactor")
    val formfactorType: String,
    @SerializedName("shape_identified")
    val shape_identified: String,
    @SerializedName("legend")
    val legend: LegendDto
)

data class CategoriesDto(
    @SerializedName("category_filter_product_base") val categoryProductBase: String,
    @SerializedName("category_products") val categoryProducts: List<ProductDto>,
    @SerializedName("category_index") val categoryIndex: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_image") val categoryImage: String,
    @SerializedName("category_price") val categoryPrice: Price?,
    @SerializedName("category_filter_watt_replaced") val categoryWattReplace: List<Int>,
    @SerializedName("category_filter_cct_code") val categoryCctCode: List<Int>,
    @SerializedName("category_energysave") val categoryEnergySave: EnergySaving,
    @SerializedName("category_filter_finish_code") val categoryFilterFinishCode: List<Int>,
    @SerializedName("category_filter_product_shape") val categoryProductShape: String
)

data class ProductDto(
    @SerializedName("product_name") var name: String,
    @SerializedName("product_index") var index: Int,
    @SerializedName("product_spec1") var spec1: Float,
    @SerializedName("product_spec3") var spec3: List<Int>,
    @SerializedName("product_spec2") var spec2: String,
    @SerializedName("product_image") var imageUrls: List<String>,
    @SerializedName("product_description") var description: String,
    @SerializedName("product_scene") var scene: String,
    @SerializedName("product_category_name") var categoryName: String,
    @SerializedName("product_SAPid_12NC") var sapID12NC: Long,
    @SerializedName("product_qty_lampscase") var qtyLampscase: Int,
    @SerializedName("product_wattage_replaced") var wattageReplaced: Int,
    @SerializedName("product_country") var country: String,
    @SerializedName("product_prio") var priority: Int,
    @SerializedName("product_wattage_claim") var wattageClaim: Float,
    @SerializedName("product_formfactor_base") var factorBase: String,
    @SerializedName("product_discount_proc") var discountProc: Int,
    @SerializedName("product_SAPid_10NC") var sapID10NC: Long,
    @SerializedName("product_dimming_code") var dimmingCode: Int,
    @SerializedName("product_finish") var finish: String,
    @SerializedName("product_promoted") var promoted: Int,
    @SerializedName("product_price_sku") var priceSku: Float,
    @SerializedName("product_price_lamp") var priceLamp: Float,
    @SerializedName("product_price_pack") var pricePack: Float,
    @SerializedName("product_formfactor_shape") var factorShape: String,
    @SerializedName("product_qty_lampsku") var qtyLampSku: Int,
    @SerializedName("product_discount_value") var discountValue: Int,
    @SerializedName("product_qty_skucase") var qtySkuCase: Int,
    @SerializedName("product_formfactor_type_code") var factorTypeCode: Int,
    @SerializedName("product_cct_code") var productCctCode: Int,
    @SerializedName("product_finish_code") var productFinishCode: Int
)


data class EnergySaving(
    @SerializedName("max_energy_saving") val maxEnergySaving: Float,
    @SerializedName("min_energy_saving") val minEnergySaving: Float
)

data class Price(
    @SerializedName("max_price") val maxPrice: Float,
    @SerializedName("min_price") val minPrice: Float
)

data class LegendDto(
    @SerializedName("product_cct_name")
    val cctFilter: List<FilterTypeDto>,

    @SerializedName("product_finish")
    val finishFilter: List<FilterTypeDto>,

    @SerializedName("product_formfactor_type")
    val lightShapeFilter: List<FilterTypeDto>
)

data class FilterTypeDto(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String
)


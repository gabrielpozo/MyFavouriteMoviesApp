package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductBrowsingListDto(
    @SerializedName("product_list") val product_list: List<ProductBrowsingDto>
)

data class ProductBrowsingDto(
    @SerializedName("product_country") val product_country: String,
    @SerializedName("product_promoted") val product_promoted: Int,
    @SerializedName("product_discount_proc") val product_discount_proc: Int,
    @SerializedName("product_discount_value") val product_discount_value: Int,
    @SerializedName("product_formfactor_base") val product_formfactor_base: String,
    @SerializedName("product_formfactor_shape") val product_formfactor_shape: String,
    @SerializedName("product_finish_code") val product_finish_code: Int,
    @SerializedName("product_cct_code") val product_cct_code: Int,
    @SerializedName("product_dimming_code") val product_dimming_code: Int,
    @SerializedName("product_finish") val product_finish: String,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("product_prio") val product_prio: Int,
    @SerializedName("product_category_name") val product_category_name: String,
    @SerializedName("product_wattage_claim") val product_wattage_claim: Double,
    @SerializedName("product_wattage_replaced") val product_wattage_replaced: Int,
    @SerializedName("product_price_pack") val product_price_pack: Float,
    @SerializedName("product_price_sku") val product_price_sku: Float,
    @SerializedName("product_price_lamp") val product_price_lamp: Float,
    @SerializedName("product_SAPid_10NC") val product_SAPid_10NC: Long,
    @SerializedName("product_SAPid_12NC") val product_SAPid_12NC: Long,
    @SerializedName("product_qty_lampsku") val product_qty_lampsku: Int,
    @SerializedName("product_qty_skucase") val product_qty_skucase: Int,
    @SerializedName("product_qty_lampscase") val product_qty_lampscase: Int,
    @SerializedName("product_description") val product_description: String,
    @SerializedName("product_formfactor_type_code") val product_formfactor_type_code: Int,
    @SerializedName("product_formfactor_type") val product_formfactor_type: String,
    @SerializedName("product_formfactor_shape_id") val product_formfactor_shape_id: Int,
    @SerializedName("product_formfactor_base_id") val product_formfactor_base_id: Int,
    @SerializedName("product_category_code") val product_category_code: Int,
    @SerializedName("energy_consumption") val energy_consumption: Int,
    @SerializedName("product_connection_code") val product_connection_code: Int,
    @SerializedName("product_scene") val product_scene: String,
    @SerializedName("product_scene_image") val product_scene_image: List<String>,
    @SerializedName("product_scene_x_options") val product_scene_x_options: List<String>,
    @SerializedName("product_scene_y_options") val product_scene_y_options: List<String>,
    @SerializedName("product_details_icons") val product_details_icons: List<String>,
    @SerializedName("product_details_icons_images") val product_details_icons_images: List<String>,
    @SerializedName("product_index") val product_index: Int,
    @SerializedName("product_image") val product_image: List<String>,
    @SerializedName("product_spec1") val product_spec1: Double,
    @SerializedName("product_spec2") val product_spec2: String,
    @SerializedName("product_spec3") val product_spec3: List<Int>
)
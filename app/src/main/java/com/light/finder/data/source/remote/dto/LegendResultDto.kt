package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName


data class LegendParsingDto(
    @SerializedName("version") val version: Int,
    @SerializedName("legend") val legend: LegendValueDto
)

data class LegendValueDto(
    @SerializedName("product_formfactor_type") val productFormFactorType: List<FormFactorTypeDto>,
    @SerializedName("product_finish") val productFinish: List<FinishTypeDto>,
    @SerializedName("product_cct_name") val productCctName: List<CctTypeDto>

)

data class FormFactorTypeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("order") val order: String
)

data class FinishTypeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("order") val order: String
)


data class CctTypeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("small_icon") val smallIcon: String,
    @SerializedName("big_icon") val bigIcon: String,
    @SerializedName("order") val order: Int,
    @SerializedName("AR_type") val arType: Int,
    @SerializedName("kelvin_spec") val kelvinSpec: KelvinSpecDto
)


data class KelvinSpecDto(
    @SerializedName("min_value") val minValue: Int,
    @SerializedName("max_value") val maxValue: Int,
    @SerializedName("default_value") val defaultValue: Int
)
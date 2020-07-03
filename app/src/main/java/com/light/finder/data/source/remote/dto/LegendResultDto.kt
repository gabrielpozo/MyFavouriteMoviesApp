package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName


data class LegendParsingDto(@SerializedName("legend") val legend: LegendValueDto)
data class LegendValueDto(@SerializedName("product_formfactor_type") val productFormFactorType: List<FormFactorTypeDto>)
data class FormFactorTypeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

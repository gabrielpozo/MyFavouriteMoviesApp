package com.light.usecases

import com.light.domain.model.CctType
import com.light.domain.model.FilterVariationCF


class GetDisplayedCctCodes {
    fun execute(
        cctTypeList: List<CctType>,
        filterVariationColors: List<FilterVariationCF>?
    ): List<CctType> {
        val cctList = mutableListOf<CctType>()
        filterVariationColors?.forEach { cccType ->
            val cctColor =
                cctTypeList.find { it.id == cccType.codeFilter }
            if (cctColor != null) {
                cctList.add(cctColor)
            }
        }
        return cctList
    }
}
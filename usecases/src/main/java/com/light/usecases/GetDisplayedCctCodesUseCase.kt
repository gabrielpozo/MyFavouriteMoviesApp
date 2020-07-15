package com.light.usecases

import com.light.domain.model.CctType
import com.light.domain.model.FilterVariationCF


class GetDisplayedCctCodesUseCase {
    fun execute(
        cctTypeList: List<CctType>,
        filterVariationColors: List<FilterVariationCF>?
    ): List<CctType> {
        val cctList = mutableListOf<CctType>()
        filterVariationColors?.forEach { filterVariationCF ->
            val cctColor =
                cctTypeList.find { cctType ->
                    cctType.id == filterVariationCF.codeFilter
                }
            if (cctColor != null) {
                cctColor.isSelected = filterVariationCF.isSelected
                cctList.add(cctColor)

            }
        }
        return cctList
    }
}

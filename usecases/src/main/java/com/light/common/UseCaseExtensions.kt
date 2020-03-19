package com.light.common

import com.light.domain.model.FilterWattage
import com.light.domain.model.Product


fun FilterWattage.isMatchSpecs(product: Product): Boolean = true
/*    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree*/


fun HashSet<FilterWattage>.removeDuplicateElements(activeOnInitList: List<FilterWattage>): List<FilterWattage> {
    val filterList = toMutableList()
    val groupedTypeList = filterList.groupBy { it.type }
    groupedTypeList.forEach { (_, value) ->
        if (value.count() == 1 && !activeOnInitList.contains(value[0])) {
            filterList.remove(value[0])
        }
    }

    return filterList

}
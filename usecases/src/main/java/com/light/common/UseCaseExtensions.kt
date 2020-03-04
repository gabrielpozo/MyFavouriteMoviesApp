package com.light.common

import com.light.domain.model.Filter
import com.light.domain.model.Product


fun Filter.isMatchSpecs(product: Product): Boolean = true
/*    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree*/


fun HashSet<Filter>.removeDuplicateElements(activeOnInitList: List<Filter>): List<Filter> {
    val filterList = toMutableList()
    val groupedTypeList = filterList.groupBy { it.type }
    groupedTypeList.forEach { (_, value) ->
        if (value.count() == 1 && !activeOnInitList.contains(value[0])) {
            filterList.remove(value[0])
        }
    }

    return filterList

}
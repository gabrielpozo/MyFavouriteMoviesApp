package com.accenture.common

import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product


fun Filter.isMatchSpecs(product: Product): Boolean =
    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree


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
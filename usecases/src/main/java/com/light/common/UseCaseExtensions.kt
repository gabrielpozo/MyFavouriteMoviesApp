package com.light.common

import com.light.domain.model.FilterColor
import com.light.domain.model.FilterFinish
import com.light.domain.model.FilterWattage
import com.light.domain.model.Product


fun FilterWattage.isMatchSpecs(product: Product): Boolean = true
/*    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree*/


fun HashSet<FilterWattage>.removeDuplicateElements(activeOnInitList: List<FilterWattage>): List<FilterWattage> {
    val filterList = toMutableList()
/*

    val groupedTypeList = filterList.groupBy { it.type }
    groupedTypeList.forEach { (_, value) ->
        if (value.count() == 1 && !activeOnInitList.contains(value[0])) {
            filterList.remove(value[0])
        }
    }
*/


    return filterList
}

fun HashSet<FilterWattage>.checkIsTherePreviousActiveState(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.nameFilter == product.wattageReplaced.toString()) {
                if(filterWattage.isAvailable || filterWattage.isSelected){
                    return true
                }
            }
        }
    }
    return false
}

fun HashSet<FilterColor>.checkThereIsPreviousActiveStateC(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.nameFilter == product.colorCctCode) {
                if(filterWattage.isAvailable || filterWattage.isSelected){
                    return true
                }
            }
        }
    }
    return false
}

fun HashSet<FilterFinish>.checkThereIsPreviousActiveStateF(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.nameFilter == product.finish) {
                if(filterWattage.isAvailable || filterWattage.isSelected){
                    return true
                }
            }
        }
    }
    return false
}
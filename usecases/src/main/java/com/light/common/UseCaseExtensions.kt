package com.light.common


import com.light.domain.model.FilterVariation
import com.light.domain.model.Product


fun FilterVariation.isMatchSpecs(product: Product): Boolean = true
/*    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree*/


fun HashSet<FilterVariation>.removeDuplicateElements(activeOnInitList: List<FilterVariation>): List<FilterVariation> {
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

fun HashSet<FilterVariation>.checkIsTherePreviousActiveState(product: Product): Boolean {
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

fun HashSet<FilterVariation>.checkThereIsPreviousActiveStateC(product: Product): Boolean {
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

fun HashSet<FilterVariation>.checkThereIsPreviousActiveStateF(product: Product): Boolean {
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
package com.light.common


import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE


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

fun List<Product>.setSelectedProductToCompatibleList(
    productSelected: Product,
    filter: FilterVariation
) {

    forEach { product ->
        when (filter.type) {
            TYPE.WATTAGE -> {
                if (product.wattageReplaced.toString() == filter.nameFilter) {
                    if (productSelected.colorCctCode == product.colorCctCode && productSelected.finish == product.finish) {
                        product.isSelected = true
                    }
                }
            }

            TYPE.COLOR -> {
                if (product.colorCctCode == filter.nameFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.finish == product.finish) {
                        product.isSelected = true
                    }
                }
            }
            TYPE.FINISH -> {
                if (product.finish.toString() == filter.nameFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.colorCctCode == product.colorCctCode) {
                        product.isSelected = true
                    }
                }
            }
        }
    }

}


fun List<Product>.setSelectedProduct(
    productSelected: Product,
    filter: FilterVariation
) {

    forEach { product ->
        when (filter.type) {
            TYPE.WATTAGE -> {
                if (product.wattageReplaced.toString() == filter.nameFilter) {
                    if (productSelected.colorCctCode == product.colorCctCode || productSelected.finish == product.finish) {
                        product.isSelected = true
                    }
                }
            }

            TYPE.COLOR -> {
                if (product.wattageReplaced.toString() == filter.nameFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.finish == product.finish) {
                        product.isSelected = true
                    }
                }
            }
            TYPE.FINISH -> {
                if (product.wattageReplaced.toString() == filter.nameFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.colorCctCode == product.colorCctCode) {
                        product.isSelected = true
                    }
                }
            }
        }
    }

}
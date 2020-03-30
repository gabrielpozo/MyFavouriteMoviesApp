package com.light.common


import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE


fun FilterVariationCF.isMatchSpecs(product: Product): Boolean = true
/*    nameFilter == product.productSpecOne
            || nameFilter == product.productScene
            || nameFilter == product.productSpecThree*/


fun HashSet<FilterVariationCF>.removeDuplicateElements(activeOnInitList: List<FilterVariationCF>): List<FilterVariationCF> {
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

fun HashSet<FilterVariationCF>.checkIsTherePreviousActiveState(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.codeFilter == product.wattageReplaced) {
                if (filterWattage.isAvailable || filterWattage.isSelected) {
                    return true
                }
            }
        }
    }
    return false
}

fun HashSet<FilterVariationCF>.checkThereIsPreviousActiveStateC(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.codeFilter == product.colorCctCode) {
                if (filterWattage.isAvailable || filterWattage.isSelected) {
                    return true
                }
            }
        }
    }
    return false
}

fun HashSet<FilterVariationCF>.checkThereIsPreviousActiveStateF(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach { filterWattage ->
            if (filterWattage.codeFilter == product.productFinishCode) {
                if (filterWattage.isAvailable || filterWattage.isSelected) {
                    return true
                }
            }
        }
    }
    return false
}

fun List<Product>.setSelectedProductToCompatibleList(
    productSelected: Product,
    filter: FilterVariationCF
) {

    forEach { product ->
        when (filter.type) {
            TYPE.WATTAGE -> {
                if (product.wattageReplaced == filter.codeFilter) {
                    if (productSelected.colorCctCode == product.colorCctCode && productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                    }
                }
            }

            TYPE.COLOR -> {
                if (product.colorCctCode == filter.codeFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                    }
                }
            }
            TYPE.FINISH -> {
                if (product.productFinishCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.colorCctCode == product.colorCctCode) {
                        product.isSelected = true
                    }
                }
            }
        }
    }

}


fun List<Product>.setSelectedProductToIncompatibleList(
    productSelected: Product,
    filter: FilterVariationCF
) {

    forEach { product ->
        when (filter.type) {
            TYPE.WATTAGE -> {
                if (productSelected.wattageReplaced == filter.codeFilter) {
                    if (productSelected.colorCctCode == product.colorCctCode || productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                    }
                }
            }

            TYPE.COLOR -> {
                if (productSelected.colorCctCode == filter.codeFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                    }
                }
            }
            TYPE.FINISH -> {
                if (productSelected.productFinishCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.colorCctCode == product.colorCctCode) {
                        product.isSelected = true
                    }
                }
            }
        }
    }

}
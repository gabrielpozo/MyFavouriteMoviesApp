package com.light.common


import com.light.domain.model.*


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

fun HashSet<FilterVariationCF>.checkThereIsPreviousActiveStateCon(product: Product): Boolean {
    if (!product.isAvailable) {
        forEach {
            if (it.codeFilter == product.productConnectionCode) {
                if (it.isAvailable || it.isSelected) {
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
                    if (productSelected.colorCctCode == product.colorCctCode && productSelected.productFinishCode == product.productFinishCode
                        && productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }

            TYPE.COLOR -> {
                if (product.colorCctCode == filter.codeFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.productFinishCode == product.productFinishCode
                        && productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }
            TYPE.FINISH -> {
                if (product.productFinishCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.colorCctCode == product.colorCctCode
                        && productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }
            TYPE.CONNECTIVITY -> {
                if (product.productConnectionCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced && productSelected.colorCctCode == product.colorCctCode
                        && productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                        return
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
                if (product.wattageReplaced == filter.codeFilter) {
                    if (productSelected.colorCctCode == product.colorCctCode || productSelected.productFinishCode == product.productFinishCode
                        || productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }

            TYPE.COLOR -> {
                if (product.colorCctCode == filter.codeFilter) {

                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.productFinishCode == product.productFinishCode
                        || productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }
            TYPE.FINISH -> {
                if (product.productFinishCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.colorCctCode == product.colorCctCode
                        || productSelected.productConnectionCode == product.productConnectionCode) {
                        product.isSelected = true
                        return
                    }
                }
            }
            TYPE.CONNECTIVITY -> {
                if (product.productConnectionCode == filter.codeFilter) {
                    if (productSelected.wattageReplaced == product.wattageReplaced || productSelected.colorCctCode == product.colorCctCode
                        || productSelected.productFinishCode == product.productFinishCode) {
                        product.isSelected = true
                        return
                    }
                }
            }
        }
    }

}

fun List<CctType>.removeUnusedColors(): List<CctType> {
    val unusedColorList = toMutableList()
    unusedColorList.removeAll { it.id == 6 || it.id == 7 }
    return unusedColorList
}


val emptyCctType = CctType(-1, "", "", "", -1, -1, KelvinSpec(-1, -1, -1))
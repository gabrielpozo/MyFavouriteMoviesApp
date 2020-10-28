package com.light.finder.extensions

import com.light.domain.model.Category
import com.light.domain.model.ProductCategoryName

fun List<Category>.convertCategoryListToCategoryString(loadProductCategoryName: List<ProductCategoryName>): String {
    var nameCategory = ""
    var listHashSet = HashSet<Int>()
    forEach {
        listHashSet.add(it.categoryProducts[0].produtCategoryCode)
    }

    listHashSet.forEach { categoryCode ->
        nameCategory = nameCategory + getLegendCategoryName(
            categoryCode,
            loadProductCategoryName
        ) + " \u2022 "
    }

    return if (nameCategory.isNotEmpty()) {
        nameCategory.removeRange(nameCategory.length - 2, nameCategory.length - 1)
    } else nameCategory

}

fun List<String>.convertCategoryListToShapeString(): String {
    var nameShape = ""
    var listHashSet = LinkedHashSet<String>()
    forEach {
        listHashSet.add(it)
    }
    listHashSet.forEach { categoryShape -> nameShape = "$nameShape$categoryShape \u2022 " }

    return if (nameShape.isNotEmpty()) {
        nameShape.removeRange(nameShape.length - 2, nameShape.length - 1)
    } else nameShape
}
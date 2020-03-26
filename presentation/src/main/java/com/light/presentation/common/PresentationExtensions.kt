package com.light.presentation.common

import com.light.domain.model.FilterColor
import com.light.domain.model.FilterFinish
import com.light.domain.model.FilterWattage
import com.light.domain.model.Product


fun FilterWattage.setSelectedProduct(dataProducts: List<Product>): Product? {
    dataProducts.find { product ->
        product.wattageReplaced.toString() == nameFilter
    }.also { product ->
        product?.let {
            it.isSelected = true
            return product
        }
    }
    return null
}


fun FilterColor.setSelectedProduct(dataProducts: List<Product>): Product? {
    dataProducts.find { product ->
        product.wattageReplaced.toString() == nameFilter
    }.also { product ->
        product?.let {
            it.isSelected = true
            return product
        }
    }
    return null
}

fun FilterFinish.setSelectedProduct(dataProducts: List<Product>): Product? {
    dataProducts.find { product ->
        product.wattageReplaced.toString() == nameFilter
    }.also { product ->
        product?.let {
            it.isSelected = true
            return product
        }
    }
    return null
}
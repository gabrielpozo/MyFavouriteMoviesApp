package com.light.presentation.common

import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE


/*fun FilterVariation.setSelectedProduct(dataProducts: List<Product>): Product? {
    dataProducts.find { product ->
        product.wattageReplaced.toString() == nameFilter
    }.also { product ->
        product?.let {
            it.isSelected = true
            return product
        }
    }
    return null
}*/


fun FilterVariationCF.setSelectedProduct(dataProducts: List<Product>): Product? {
    val product = dataProducts.find {
        it.isSelected
    }
    if (product == null) {
        dataProducts.find { product ->
            when (type) {
                TYPE.WATTAGE -> {
                    product.wattageReplaced == codeFilter
                }
                TYPE.COLOR -> {
                    product.colorCctCode == codeFilter
                }
                TYPE.FINISH -> {
                    product.productFinishCode == codeFilter
                }
                TYPE.CONNECTIVITY -> {
                    product.productConnectionCode == codeFilter
                }
            }
        }.also { product ->
            product?.let {
                it.isSelected = true
                return product
            }
        }
    }
    return product
}


fun getSelectedProduct(dataProducts: List<Product>): Product? = dataProducts.find {
    it.isSelected
}



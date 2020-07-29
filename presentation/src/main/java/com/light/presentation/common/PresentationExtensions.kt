package com.light.presentation.common

import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.ShapeBrowsing
import com.light.domain.model.TYPE


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


fun List<ShapeBrowsing>.isProductsShapeSelected(): Boolean =
    find { it.isSelected } != null

fun List<ShapeBrowsing>.setSelectedProductShape(productShape: ShapeBrowsing) {
    find { it.id == productShape.id }?.let {
        it.isSelected = productShape.isSelected
    }
}

fun List<ShapeBrowsing>.resetShapeProductList(){
    forEach {
        it.isSelected = false
    }

}
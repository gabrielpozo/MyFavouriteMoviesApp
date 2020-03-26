package com.light.usecases

import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetNewSelectedProduct : BaseUseCase<List<Product>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productList: List<Product> = params[0] as List<Product>
        val filter: FilterVariation = params[1] as FilterVariation


        //1.We get the product selected
        val productSelected = productList.find {
            it.isSelected
        }
        //reset list
        productList.forEach {
            it.isAvailable = false
            it.isSelected = false
        }

        //2. check if there is one option with one of the remain variations
        productList.setSelectedProduct(productSelected!!, filter)

        return DataState.Success(productList)
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
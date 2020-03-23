package com.light.usecases

import com.light.domain.model.FilterWattage
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetNewSelectedProduct : BaseUseCase<List<Product>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productList: List<Product> = params[0] as List<Product>
        val filter: FilterWattage = params[1] as FilterWattage
        //get the product selected
        val productSelected = productList.find {
            it.isSelected
        }
        //set the selected to false
        productList.find {
            it.isSelected
        }?.isSelected = false

        productList.forEach { product ->
            product.isAvailable = false
            if (product.colorCctCode == productSelected?.colorCctCode
                && product.finish == product.finish && product.wattageReplaced.toString() == filter.nameFilter
            ) {
                product.isSelected = true
            }
        }

        return DataState.Success(productList)
    }

}
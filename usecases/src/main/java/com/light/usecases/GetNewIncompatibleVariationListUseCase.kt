package com.light.usecases

import com.light.common.setSelectedProduct
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetNewIncompatibleVariationListUseCase : BaseUseCase<List<Product>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productList: List<Product> = params[0] as List<Product>
        val filter: FilterVariation = params[1] as FilterVariation


        //1.We get the product selected
        val productSelected = productList.find {
            it.isSelected
        }
        //2.reset list
        productList.forEach {
            it.isAvailable = false
            it.isSelected = false
        }

        //3. check if there is one option with one of the remain variations
        productList.setSelectedProduct(productSelected!!, filter)

        return DataState.Success(productList)
    }
}
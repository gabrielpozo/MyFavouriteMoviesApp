package com.light.usecases

import com.light.common.isMatchSpecs
import com.light.domain.model.Filter
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetProductsFilteredUseCase : BaseUseCase<List<Product>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productDataList = params[0] as List<Product>
        val initFilterList = params[1] as List<Filter>
        val productsFiltered = mutableListOf<Product>()
        val filterActiveList = initFilterList.filter { it.isActive }

        productDataList.forEach { product ->
            var count = 0
            while (count < filterActiveList.size && filterActiveList[count].isMatchSpecs(product)) {
                count++
            }

            if (count == filterActiveList.size) {
                productsFiltered.add(product)
            }
        }

        return DataState.Success(if (productsFiltered.isEmpty()) productDataList else productsFiltered)
    }


}



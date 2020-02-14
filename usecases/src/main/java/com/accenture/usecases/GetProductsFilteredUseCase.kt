package com.accenture.usecases

import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetProductsFilteredUseCase : BaseUseCase<List<Product>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productDataList = params[0] as List<Product>
        val filterList = params[1] as List<Filter>
        val productsFiltered = mutableListOf<Product>()
        val filterHashSet = filterList.toHashSet()

        filterHashSet.map { filter ->
            if (filter.isActive) {
                productDataList.map { product ->
                    if (filter.nameFilter == product.productSpecOne || filter.nameFilter == product.productScene
                        || filter.nameFilter == product.productSpecThree
                    ) {
                        productsFiltered.add(product)
                    }
                }
            }
        }

        return DataState.Success(productsFiltered)
    }
}


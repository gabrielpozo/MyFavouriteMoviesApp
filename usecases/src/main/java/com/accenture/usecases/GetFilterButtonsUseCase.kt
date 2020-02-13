package com.accenture.usecases

import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.domain.state.DataState


class GetFilterButtonsUseCase : BaseUseCase<List<Filter>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
        val productList: List<Product> = params[0] as List<Product>
       // val filter: Filter = params[1] as Filter


        val filterList = productList.map { product ->
            product.productScene.isNotEmpty().let {
                Filter(nameFilter = product.productScene)
            }

            product.productSpecOne.isNotEmpty().let {
                Filter(nameFilter = product.productScene)
            }

            product.productSpecThree.isNotEmpty().let {
                Filter(nameFilter = product.productSpecThree)
            }
        }


        if (params.size > 1) {
            val filter = params[1] as Filter
            filterList.find { it.nameFilter == filter.nameFilter }
                ?.copy(isActive = !filter.isActive)
            //filterList.add(filter.copy(isActive = !filter.isActive))
        }
        return DataState.Success(filterList)
    }

}
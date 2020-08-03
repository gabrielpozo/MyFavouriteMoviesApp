package com.light.usecases

import com.light.domain.model.Product
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetDetailUseCase : BaseUseCase<List<Product>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productDataList = params[0] as List<Product>
        val product = params[1] as Product


        return DataState.Success(productDataList.sortedBy { it.productPrio }.take(0))
    }
}

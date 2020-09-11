package com.light.usecases

import com.light.common.setSelectedProductToCompatibleList
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetNewCompatibleVariationListUseCase : BaseScanningUseCase<List<Product>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Product>> {
        val productList: List<Product> = params[0] as List<Product>
        val filter: FilterVariationCF = params[1] as FilterVariationCF
        //check for the data products
        val productSelected = productList.find {
            it.isSelected
        }

        productList.forEach {
            it.isAvailable = false
            it.isSelected = false
        }
        if (productSelected != null) {
            productList.setSelectedProductToCompatibleList(productSelected, filter)
        }

        return DataState.Success(productList)
    }

}



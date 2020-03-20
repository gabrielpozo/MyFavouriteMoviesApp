package com.light.usecases

import com.light.domain.model.FilterFinish
import com.light.domain.model.Product
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetFinishVariationsUseCase : BaseUseCase<List<FilterFinish>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterFinish>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterFinish>()

        // 1: Check Availables!
        //which finish variations are available for this wattages and colors
        val productSelected = productList.find { it.isSelected }
        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.colorCctCode == productSelected.colorCctCode) {
                it.isAvailable = true
            }
        }

        productList.forEach { product ->
            filterHashSet.add(
                FilterFinish(
                    nameFilter = product.finish,
                    isSelected = product.isSelected,
                    isAvailable = product.isAvailable
                )
            )
        }


        return DataState.Success(filterHashSet.toList())
    }

}
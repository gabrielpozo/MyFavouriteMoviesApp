package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateF
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
        productSelected?.let {
            filterHashSet.add(
                FilterFinish(
                    nameFilter = productSelected.finish,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.colorCctCode == productSelected.colorCctCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterFinish(
                        nameFilter = it.finish,
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable
                    )
                )
            }
        }

        productList.forEach { product ->
            if (!filterHashSet.checkThereIsPreviousActiveStateF(product)) {
                filterHashSet.add(
                    FilterFinish(
                        nameFilter = product.finish,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable
                    )
                )
            }
        }


        return DataState.Success(filterHashSet.toList())
    }

}
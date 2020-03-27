package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateF
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetFinishVariationsUseCase : BaseUseCase<List<FilterVariation>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariation>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariation>()

        // 1: Check Availables!
        //which finish variations are available for this wattages and colors
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariation(
                    nameFilter = productSelected.finish,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.FINISH
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.colorCctCode == productSelected.colorCctCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = it.finish,
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable,
                        type = TYPE.FINISH
                    )
                )
            }
        }

        productList.forEach { product ->
            if (!filterHashSet.checkThereIsPreviousActiveStateF(product)) {
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = product.finish,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.FINISH
                    )
                )
            }
        }


        return DataState.Success(filterHashSet.toList())
    }

}
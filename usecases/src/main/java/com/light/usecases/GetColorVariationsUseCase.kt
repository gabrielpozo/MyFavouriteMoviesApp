package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateC
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetColorVariationsUseCase : BaseUseCase<List<FilterVariation>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariation>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariation>()

        // 1: Check Availables!
        //which colors are available for this wattages and finish variations
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariation(
                    nameFilter = productSelected.colorCctCode,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.COLOR
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.finish == productSelected.finish) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = it.colorCctCode,
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable,
                        type = TYPE.COLOR
                    )
                )
            }
        }

        productList.forEach { product ->
            //do some checks here before adding???
            if (!filterHashSet.checkThereIsPreviousActiveStateC(product)) {
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = product.colorCctCode,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.COLOR
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.toList())
    }

}


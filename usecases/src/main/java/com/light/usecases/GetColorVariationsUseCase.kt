package com.light.usecases

import com.light.domain.model.FilterColor
import com.light.domain.model.Product
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetColorVariationsUseCase : BaseUseCase<List<FilterColor>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterColor>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterColor>()

        // 1: Check Availables!
        //which colors are available for this wattages and finish variations
        val productSelected = productList.find { it.isSelected }
        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.finish == productSelected.finish) {
                it.isAvailable = true
            }
        }

        productList.forEach { product ->
            //do some checks here before???
            if (product.isSelected || product.isAvailable) {
                filterHashSet.add(
                    FilterColor(
                        nameFilter = product.colorCctCode,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.toList())
    }

}

private fun checkifThereisAnActiveState(productList: List<Product>, wattageReplace: Int): Boolean {
    val product = productList.find {
        it.isAvailable || it.isSelected
    }

    return true

}
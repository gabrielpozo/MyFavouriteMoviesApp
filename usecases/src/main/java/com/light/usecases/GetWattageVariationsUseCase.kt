package com.light.usecases

import com.light.common.checkIsTherePreviousActiveState
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetWattageVariationsUseCase : BaseUseCase<List<FilterVariation>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariation>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariation>()

        // 1: Check Availables!
        //which wattages are available for this colors and ALSO for the finish variations
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariation(
                    nameFilter = productSelected.wattageReplaced.toString(),
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.WATTAGE
                )
            )
        }

        productList.forEach {
            if (it.colorCctCode == productSelected?.colorCctCode && it.finish == productSelected.finish) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = it.wattageReplaced.toString(),
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable,
                        type = TYPE.WATTAGE
                    )
                )
            }
        }

        productList.forEach { product ->
            if (!filterHashSet.checkIsTherePreviousActiveState(product)) {
                filterHashSet.add(
                    FilterVariation(
                        nameFilter = product.wattageReplaced.toString(),
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.WATTAGE
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.nameFilter?.toInt() ?: -1) - (f2?.nameFilter?.toInt() ?: -1)
        }).toList())
    }

}
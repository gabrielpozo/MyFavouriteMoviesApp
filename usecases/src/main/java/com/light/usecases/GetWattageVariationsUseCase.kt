package com.light.usecases

import com.light.domain.model.FilterWattage
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetWattageVariationsUseCase : BaseUseCase<List<FilterWattage>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterWattage>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterWattage>()

        // 1: Check Availables!
        //which wattages are available for this colors and ALSO for the finish variations
        val productSelected = productList.find { it.isSelected }
        productList.forEach {
            if (it.colorCctCode == productSelected?.colorCctCode && it.finish == productSelected.finish) {
                it.isAvailable = true
            }
        }

        productList.forEach { product ->
            filterHashSet.add(
                FilterWattage(
                    nameFilter = product.wattageReplaced.toString(),
                    isSelected = product.isSelected,
                    isAvailable = product.isAvailable
                )
            )
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.nameFilter?.toInt() ?: -1) - (f2?.nameFilter?.toInt() ?: -1)
        }).toList())
    }

}
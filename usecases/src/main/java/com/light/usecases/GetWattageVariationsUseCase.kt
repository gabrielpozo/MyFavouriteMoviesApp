package com.light.usecases

import com.light.common.checkIsTherePreviousActiveState
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
        productSelected?.let {
            filterHashSet.add(FilterWattage(
                nameFilter = productSelected.wattageReplaced.toString(),
                isSelected = productSelected.isSelected,
                isAvailable = productSelected.isAvailable
            ))
        }

        productList.forEach {
            if (it.colorCctCode == productSelected?.colorCctCode && it.finish == productSelected.finish) {
                it.isAvailable = true
                filterHashSet.add(FilterWattage(
                    nameFilter = it.wattageReplaced.toString(),
                    isSelected = it.isSelected,
                    isAvailable = it.isAvailable
                ))
            }
        }


        productList.forEach { product ->
            if (!filterHashSet.checkIsTherePreviousActiveState(product)) {
                filterHashSet.add(
                    FilterWattage(
                        nameFilter = product.wattageReplaced.toString(),
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable
                    )
                )
            }
        }


        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.nameFilter?.toInt() ?: -1) - (f2?.nameFilter?.toInt() ?: -1)
        }).toList())
    }

}
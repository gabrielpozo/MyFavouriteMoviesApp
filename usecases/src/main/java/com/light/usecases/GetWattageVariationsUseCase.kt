package com.light.usecases

import com.light.common.checkIsTherePreviousActiveState
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetWattageVariationsUseCase : BaseScanningUseCase<List<FilterVariationCF>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariationCF>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariationCF>()

        // 1: Check Availables!
        //which wattages are available for this colors and ALSO for the finish variations
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariationCF(
                    codeFilter = productSelected.wattageReplaced,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.WATTAGE
                )
            )
        }

        productList.forEach {
            if (it.colorCctCode == productSelected?.colorCctCode &&
                it.productFinishCode == productSelected.productFinishCode
                && it.productConnectionCode == productSelected.productConnectionCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariationCF(
                        codeFilter = it.wattageReplaced,
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
                    FilterVariationCF(
                        codeFilter = product.wattageReplaced,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.WATTAGE
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.codeFilter ?: -1) - (f2?.codeFilter ?: -1)
        }).toList())
    }

}
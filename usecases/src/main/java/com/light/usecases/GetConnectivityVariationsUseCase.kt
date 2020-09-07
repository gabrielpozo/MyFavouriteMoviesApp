package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateCon
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetConnectivityVariationsUseCase : BaseScanningUseCase<List<FilterVariationCF>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariationCF>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariationCF>()

        // 1: Check Availables!
        //which finish variations are available for this wattages and colors
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariationCF(
                    codeFilter = productSelected.productConnectionCode,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.CONNECTIVITY
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.colorCctCode == productSelected.colorCctCode
                && it.productFinishCode == productSelected.productFinishCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariationCF(
                        codeFilter = it.productConnectionCode,
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable,
                        type = TYPE.CONNECTIVITY
                    )
                )
            }
        }
        //TODO create method for connectivity
        productList.forEach { product ->
            if (!filterHashSet.checkThereIsPreviousActiveStateCon(product)) {
                filterHashSet.add(
                    FilterVariationCF(
                        codeFilter = product.productConnectionCode,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.CONNECTIVITY
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.codeFilter ?: -1) - (f2?.codeFilter ?: -1)
        }).toList())
    }

}

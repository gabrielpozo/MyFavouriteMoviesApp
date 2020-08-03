package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateF
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetFinishVariationsUseCase : BaseUseCase<List<FilterVariationCF>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariationCF>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariationCF>()

        // 1: Check Availables!
        //which finish variations are available for this wattages and colors
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariationCF(
                    codeFilter = productSelected.productFinishCode,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.FINISH
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced
                && it.colorCctCode == productSelected.colorCctCode
                && it.productConnectionCode == productSelected.productConnectionCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariationCF(
                        codeFilter = it.productFinishCode,
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
                    FilterVariationCF(
                        codeFilter = product.productFinishCode,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.FINISH
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.codeFilter ?: -1) - (f2?.codeFilter ?: -1)
        }).toList())
    }

}

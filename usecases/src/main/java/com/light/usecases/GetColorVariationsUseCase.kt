package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateC
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.model.TYPE
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetColorVariationsUseCase : BaseUseCase<List<FilterVariationCF>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariationCF>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariationCF>()

        // 1: Check Availables!
        //which colors are available for this wattages and finish variations
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(
                FilterVariationCF(
                    codeFilter = productSelected.colorCctCode,
                    isSelected = productSelected.isSelected,
                    isAvailable = productSelected.isAvailable,
                    type = TYPE.COLOR
                )
            )
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced &&
                it.productFinishCode == productSelected.productFinishCode
                && it.productConnectionCode == productSelected.productConnectionCode) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterVariationCF(
                        codeFilter = it.colorCctCode,
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
                    FilterVariationCF(
                        codeFilter = product.colorCctCode,
                        isSelected = product.isSelected,
                        isAvailable = product.isAvailable,
                        type = TYPE.COLOR
                    )
                )
            }
        }

        return DataState.Success(filterHashSet.sortedWith(Comparator { f1, f2 ->
            (f1?.codeFilter ?: -1) - (f2?.codeFilter ?: -1)
        }).toList())
    }

}



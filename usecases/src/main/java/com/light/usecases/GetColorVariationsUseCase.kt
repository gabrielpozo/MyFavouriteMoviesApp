package com.light.usecases

import com.light.common.checkThereIsPreviousActiveStateC
import com.light.domain.model.FilterColor
import com.light.domain.model.FilterWattage
import com.light.domain.model.Product
import com.light.domain.state.DataState
import java.util.logging.Filter

@Suppress("UNCHECKED_CAST")
class GetColorVariationsUseCase : BaseUseCase<List<FilterColor>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterColor>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterColor>()

        // 1: Check Availables!
        //which colors are available for this wattages and finish variations
        val productSelected = productList.find { it.isSelected }
        productSelected?.let {
            filterHashSet.add(FilterColor(
                nameFilter = productSelected.colorCctCode,
                isSelected = productSelected.isSelected,
                isAvailable = productSelected.isAvailable
            ))
        }

        productList.forEach {
            if (it.wattageReplaced == productSelected?.wattageReplaced && it.finish == productSelected.finish) {
                it.isAvailable = true
                filterHashSet.add(
                    FilterColor(
                        nameFilter = it.colorCctCode,
                        isSelected = it.isSelected,
                        isAvailable = it.isAvailable
                    )
                )
            }
        }

        productList.forEach { product ->
            //do some checks here before adding???
                if (!filterHashSet.checkThereIsPreviousActiveStateC(product)) {
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


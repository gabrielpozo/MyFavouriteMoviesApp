package com.light.usecases

import com.light.common.removeDuplicateElements
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetFilterButtonsUseCase : BaseScanningUseCase<List<FilterVariationCF>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariationCF>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariationCF>()
        val initFilterList = params[1] as List<FilterVariationCF>


        productList.forEach { product ->
            /*filterHashSet.add(Filter(nameFilter = product.productSpecOne, type = TYPE.SPEC1))
            filterHashSet.add(Filter(nameFilter = product.productSpecThree, type = TYPE.SPEC3))
            filterHashSet.add(Filter(nameFilter = product.productScene, type = TYPE.PRODUCT_SCENE))*/
        }

        val activeOnInitList = initFilterList.filter { it.isSelected }

        if (activeOnInitList.isNotEmpty()) {
            activeOnInitList.map { filterOnInitList ->
                filterHashSet.find { filterOnInitList.codeFilter == it.codeFilter }?.isSelected =
                    true
            }
        }

        return DataState.Success(filterHashSet.removeDuplicateElements(activeOnInitList))
    }

}

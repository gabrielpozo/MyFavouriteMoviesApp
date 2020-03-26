package com.light.usecases

import com.light.common.removeDuplicateElements
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetFilterButtonsUseCase : BaseUseCase<List<FilterVariation>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariation>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterVariation>()
        val initFilterList = params[1] as List<FilterVariation>


        productList.forEach { product ->
            /*filterHashSet.add(Filter(nameFilter = product.productSpecOne, type = TYPE.SPEC1))
            filterHashSet.add(Filter(nameFilter = product.productSpecThree, type = TYPE.SPEC3))
            filterHashSet.add(Filter(nameFilter = product.productScene, type = TYPE.PRODUCT_SCENE))*/
        }

        val activeOnInitList = initFilterList.filter { it.isSelected }

        if (activeOnInitList.isNotEmpty()) {
            activeOnInitList.map { filterOnInitList ->
                filterHashSet.find { filterOnInitList.nameFilter == it.nameFilter }?.isSelected =
                    true
            }
        }

        return DataState.Success(filterHashSet.removeDuplicateElements(activeOnInitList))
    }

}

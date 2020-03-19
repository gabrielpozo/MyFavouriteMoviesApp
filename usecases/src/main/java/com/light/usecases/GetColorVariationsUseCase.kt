package com.light.usecases

import com.light.domain.model.FilterColor
import com.light.domain.model.Product
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetColorVariationsUseCase : BaseUseCase<List<FilterColor>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterColor>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterColor>()

        productList.forEach { product ->
            filterHashSet.add(
                FilterColor(
                    nameFilter = product.colorCctCode,
                    isSelected = product.isSelected
                )
            )
        }
/*        val filterHashSet = hashSetOf<FilterWattage>()
        val initFilterList = params[1] as List<FilterWattage>


        productList.forEach { product ->
            *//*filterHashSet.add(Filter(nameFilter = product.productSpecOne, type = TYPE.SPEC1))
            filterHashSet.add(Filter(nameFilter = product.productSpecThree, type = TYPE.SPEC3))
            filterHashSet.add(Filter(nameFilter = product.productScene, type = TYPE.PRODUCT_SCENE))*//*
        }

        val activeOnInitList = initFilterList.filter { it.isActive }

        if (activeOnInitList.isNotEmpty()) {
            activeOnInitList.map { filterOnInitList ->
                filterHashSet.find { filterOnInitList.nameFilter == it.nameFilter }?.isActive =
                    true
            }
        }*/

        return DataState.Success(filterHashSet.toList())
    }

}
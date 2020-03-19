package com.light.usecases

import com.light.domain.model.FilterFinish
import com.light.domain.model.Product
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetFinishVariationsUseCase : BaseUseCase<List<FilterFinish>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterFinish>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<FilterFinish>()

        productList.forEach { product ->
            filterHashSet.add(
                FilterFinish(
                    nameFilter = product.finish,
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
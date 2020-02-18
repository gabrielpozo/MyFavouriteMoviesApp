package com.accenture.usecases

import com.accenture.common.removeDuplicateElements
import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.domain.model.TYPE
import com.accenture.domain.state.DataState
import sun.rmi.runtime.Log


@Suppress("UNCHECKED_CAST")
class GetFilterButtonsUseCase : BaseUseCase<List<Filter>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<Filter>()
        val initFilterList = params[1] as List<Filter>


        productList.forEach { product ->
            filterHashSet.add(Filter(nameFilter = product.productSpecOne, type = TYPE.SPEC1))
            filterHashSet.add(Filter(nameFilter = product.productSpecThree, type = TYPE.SPEC3))
            filterHashSet.add(Filter(nameFilter = product.productScene, type = TYPE.PRODUCT_SCENE))
        }

        val activeOnInitList = initFilterList.filter { it.isActive }

        if (activeOnInitList.isNotEmpty()) {
            activeOnInitList.map { filterOnInitList ->
                filterHashSet.find { filterOnInitList.nameFilter == it.nameFilter }?.isActive =
                    true
            }
        }
      /*  val reomeo = filterHashSet.filter { it.isActive }

        reomeo*/


        return DataState.Success(filterHashSet.removeDuplicateElements(activeOnInitList))
    }

}

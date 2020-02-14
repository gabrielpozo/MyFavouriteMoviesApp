package com.accenture.usecases

import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetFilterButtonsUseCase : BaseUseCase<List<Filter>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
        val productList: List<Product> = params[0] as List<Product>
        val filterHashSet = hashSetOf<Filter>()

        //filter List must be a hashSet
        productList.map { product ->
            product.productScene.isNotEmpty().let {
                filterHashSet.add(Filter(nameFilter = product.productScene, type = "whatever"))
            }

            product.productSpecOne.isNotEmpty().let {
                filterHashSet.add(Filter(nameFilter = product.productSpecOne, type = "whatever"))
            }

            product.productSpecThree.isNotEmpty().let {
                filterHashSet.add(Filter(nameFilter = product.productSpecThree, type = "whatever"))
            }
        }


        return DataState.Success(filterHashSet.toList())
    }

}
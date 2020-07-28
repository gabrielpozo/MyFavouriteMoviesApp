package com.light.usecases

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState


class GetBrowseProductsResultUseCase(private val productBrowsingRepository: ProductBrowsingRepository) {

    suspend fun execute(
        onSuccess: (Message) -> Unit = {},
        onEmpty: () -> Unit = {},
        shapeBrowsingList: List<ShapeBrowsing>
    ) {
        when(val result = productBrowsingRepository.getProductBrowsingRepository(shapeBrowsingList)){
            is DataState.Success ->{
                onSuccess.invoke(result.data)
            }
            else -> {
                onEmpty.invoke()
            }
        }

    }


}
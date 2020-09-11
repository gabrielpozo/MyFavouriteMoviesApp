package com.light.usecases

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState


class GetBrowseProductsResultUseCase(private val productBrowsingRepository: ProductBrowsingRepository) {

    suspend fun execute(
        onSuccess: (Message) -> Unit = {},
        onNoResult: (Message) -> Unit = {},
        choiceBrowsingList: List<ChoiceBrowsing>
    ) {
        when (val result =
            productBrowsingRepository.getProductBrowsingRepository(choiceBrowsingList)) {
            is DataState.Success -> {
                onSuccess.invoke(result.data)
            }
            is DataState.NoResult -> {
                onNoResult.invoke(result.data)
            }
        }

    }


}
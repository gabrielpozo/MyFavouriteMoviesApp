package com.light.usecases

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.domain.model.ShapeBrowsing


class GetBrowseProductsResultUseCase(private val productBrowsingRepository: ProductBrowsingRepository) {

    suspend fun execute(
        onSuccess: (Message) -> Unit = {},
        shapeBrowsingList: List<ShapeBrowsing>
    ) {
        onSuccess.invoke(productBrowsingRepository.getProductBrowsingRepository(shapeBrowsingList))
    }


}
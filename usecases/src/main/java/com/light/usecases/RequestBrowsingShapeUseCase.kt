package com.light.usecases

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ShapeBrowsing


class RequestBrowsingShapeUseCase(private val shapeLightBulbRepository: ShapeLightBulbRepository) {
    suspend fun execute(
        onSuccess: (List<ShapeBrowsing>) -> Unit = {},
        productBaseId: Int
    ) {
        onSuccess.invoke(shapeLightBulbRepository.getShapeBrowsingProducts(productBaseId))

    }
}
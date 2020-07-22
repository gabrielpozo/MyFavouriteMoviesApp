package com.light.usecases

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ShapeBrowsing


class RequestBrowsingShapeUseCase(private val shapeLightBulbRepository: ShapeLightBulbRepository) {
    suspend fun execute(
        onSuccess: (List<ShapeBrowsing>) -> Unit = {},
        params: Array<out Any?>
    ) {
        val productBaseId: Int = params[0] as Int
        onSuccess.invoke(shapeLightBulbRepository.getShapeBrowsingProducts(productBaseId))

    }
}
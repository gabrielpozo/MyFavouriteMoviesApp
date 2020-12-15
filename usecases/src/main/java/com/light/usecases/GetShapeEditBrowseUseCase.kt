package com.light.usecases

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ShapeBrowsing

class GetShapeEditBrowseUseCase(private val shapeLightBulbRepository: ShapeLightBulbRepository) {
    suspend fun execute(): List<ShapeBrowsing> =
        shapeLightBulbRepository.getSavedShapeBrowsingList()
}
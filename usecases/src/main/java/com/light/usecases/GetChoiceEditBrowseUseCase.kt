package com.light.usecases

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ChoiceBrowsing


class GetChoiceEditBrowseUseCase(private val shapeLightBulbRepository: ShapeLightBulbRepository) {
    suspend fun execute(): List<ChoiceBrowsing> =
        shapeLightBulbRepository.getSavedChoiceBrowsingList()
}
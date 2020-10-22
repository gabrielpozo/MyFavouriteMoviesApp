package com.light.usecases

import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.FormFactorTypeBaseId


class GetFormFactorsEditBrowseUseCase(private val browseLightBulbsRepository: BrowseLightBulbsRepository) {
    suspend fun execute(): List<FormFactorTypeBaseId> =
        browseLightBulbsRepository.getFittingListForEditBrowse()

}

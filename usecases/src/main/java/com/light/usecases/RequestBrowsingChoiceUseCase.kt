package com.light.usecases

import com.light.domain.BrowseChoiceRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ShapeBrowsing


class RequestBrowsingChoiceUseCase(private val choiceLightBulbRepository: BrowseChoiceRepository) {

    fun execute(shapeBrowsingList: List<ShapeBrowsing>): List<ChoiceBrowsing> =
        choiceLightBulbRepository.getCategoryCategoriesChoice(shapeBrowsingList)
}
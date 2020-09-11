package com.light.usecases

import com.light.domain.BrowseChoiceRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ShapeBrowsing


class RequestBrowsingChoiceUseCase(private val choiceLightBulbRepository: BrowseChoiceRepository) {

    fun execute(
        onSuccess: (List<ChoiceBrowsing>) -> Unit = {},
        shapeBrowsingList: List<ShapeBrowsing>
    ) {
        onSuccess.invoke(choiceLightBulbRepository.getCategoryCategoriesChoice(shapeBrowsingList))
    }
}

package com.light.domain

import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ShapeBrowsing


interface BrowseChoiceRepository {
    fun getCategoryCategoriesChoice(browsingList: List<ShapeBrowsing>): List<ChoiceBrowsing>
}
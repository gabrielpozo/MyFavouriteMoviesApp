package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(
        choiceBrowsingList: List<ChoiceBrowsing>
    ): DataState<Message> {
        localPreferenceDataSource.saveChoiceCategories(choiceBrowsingList)
        val messageFiltered =
            localPreferenceDataSource.getFilteredProductsMessageFromChoice(choiceBrowsingList)
        val shapeList = localPreferenceDataSource.loadShapeBrowsingFiltered()
        val shapeNameMutableList = mutableListOf<String>()
        val sortedList = shapeList.sortedBy { it.order }
        sortedList.forEach { shapeBrowsing ->
            if (shapeBrowsing.isSelected) {
                shapeNameMutableList.add(shapeBrowsing.name)
            }
        }

        messageFiltered.shapeNameList = shapeNameMutableList
        messageFiltered.noSelectedCategoriesOnFiltering =
            choiceBrowsingList.find { it.isSelected } == null

        return if (messageFiltered.categories.isEmpty()) {
            DataState.NoResult(messageFiltered)
        } else {
            DataState.Success(messageFiltered)
        }
    }
}
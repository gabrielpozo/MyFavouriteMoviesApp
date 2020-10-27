package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(choiceBrowsingList: List<ChoiceBrowsing>): DataState<Message> {
        localPreferenceDataSource.saveChoiceCategories(choiceBrowsingList)
        val messageFiltered =
            localPreferenceDataSource.getFilteredProductsMessageFromChoice(choiceBrowsingList)
        val shapeList = localPreferenceDataSource.loadShapeBrowsingFiltered()
        val shapeNameMutableList = mutableListOf<String>()
        shapeList.forEach {
            if (it.isSelected) {
                shapeNameMutableList.add(it.name)
            }
        }
        //TODO("what do we do when do skipping and what do we do about the order??")
        messageFiltered.shapeNameList = if (shapeNameMutableList.isEmpty()) shapeList.map { it.name } else shapeNameMutableList
        return if (messageFiltered.categories.isEmpty()) {
            DataState.NoResult(messageFiltered)
        } else {
            DataState.Success(messageFiltered)
        }
    }
}
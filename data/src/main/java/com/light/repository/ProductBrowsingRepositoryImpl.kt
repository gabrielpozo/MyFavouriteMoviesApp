package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(choiceBrowsingList: List<ChoiceBrowsing>): DataState<Message> =
        if (choiceBrowsingList.find { it.isSelected } == null) {
            val message =
                localPreferenceDataSource.getAllProductsMessage(choiceBrowsingList[0].baseNameFitting)
            if (message.categories.isNotEmpty()) {
                DataState.Success(message)

            } else {
                DataState.NoResult(message)
            }
        } else {
            val messageFiltered =
                localPreferenceDataSource.getFilteredProductsMessageFromChoice(choiceBrowsingList)
            if (messageFiltered.categories.isEmpty()) {
                DataState.NoResult(messageFiltered)
            } else {
                DataState.Success(messageFiltered)
            }
        }

}
package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(shapeBrowsingList: List<ShapeBrowsing>): DataState<Message> =
        if (shapeBrowsingList.find { it.isSelected } == null) {
            val message =
                localPreferenceDataSource.getAllProductsMessage(shapeBrowsingList[0].baseNameFitting)
            if (message.categories.isNotEmpty()) {
                DataState.Success(message)

            } else {
                DataState.NoResult(message)

            }
        } else {
            val messageFiltered =
                localPreferenceDataSource.getFilteredProductsMessage(shapeBrowsingList)
            if (messageFiltered.categories.isEmpty()) {
                DataState.NoResult(messageFiltered)
            } else {
                DataState.Success(messageFiltered)
            }
        }

}
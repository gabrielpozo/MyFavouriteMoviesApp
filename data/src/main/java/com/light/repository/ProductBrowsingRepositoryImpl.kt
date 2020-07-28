package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource
import com.light.util.EMPTY_MESSAGE


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(shapeBrowsingList: List<ShapeBrowsing>): DataState<Message> =
        if (shapeBrowsingList.find { it.isSelected } == null) {
            DataState.Success(localPreferenceDataSource.getAllProductsMessage())
        } else {
            val messageFiltered =
                localPreferenceDataSource.getFilteredProductsMessage(shapeBrowsingList)
            if (messageFiltered.categories.isEmpty()) {
                DataState.Empty(EMPTY_MESSAGE)
            } else {
                DataState.Success(messageFiltered)
            }
        }

}
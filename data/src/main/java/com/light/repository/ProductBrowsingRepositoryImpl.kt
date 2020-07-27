package com.light.repository

import com.light.domain.ProductBrowsingRepository
import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.source.local.LocalPreferenceDataSource


class ProductBrowsingRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    ProductBrowsingRepository {
    override suspend fun getProductBrowsingRepository(shapeBrowsingList: List<ShapeBrowsing>): Message =
        localPreferenceDataSource.getProductsBrowsing(shapeBrowsingList)


}
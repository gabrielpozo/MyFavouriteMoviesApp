package com.light.repository

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource


class ShapeLightBulbsRepositoryImpl(
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : ShapeLightBulbRepository {
    override suspend fun getShapeBrowsingProducts(productBaseId: Int): List<ShapeBrowsing> {
        return localPreferenceDataSource.fittingFilteringProducts(productBaseId)
    }
}
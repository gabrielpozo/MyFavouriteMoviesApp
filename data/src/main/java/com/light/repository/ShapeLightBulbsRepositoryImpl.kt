package com.light.repository

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ShapeBrowsing
import com.light.source.local.LocalPreferenceDataSource


class ShapeLightBulbsRepositoryImpl(
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : ShapeLightBulbRepository {
    override suspend fun getShapeBrowsingProducts(productBaseId: Int): List<ShapeBrowsing> {
        val filterProducts = localPreferenceDataSource.loadProductBrowsingTags()
            .filter { it.productFormfactorBaseId == productBaseId }
        localPreferenceDataSource.saveFittingFilteredList(filterProducts)
        return localPreferenceDataSource.fittingFilteringProducts(filterProducts)
    }
}
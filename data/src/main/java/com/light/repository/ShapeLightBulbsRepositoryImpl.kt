package com.light.repository

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ShapeBrowsing
import com.light.source.local.LocalPreferenceDataSource


class ShapeLightBulbsRepositoryImpl(
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : ShapeLightBulbRepository {
    override suspend fun getShapeBrowsingProducts(productBaseId: Int): List<ShapeBrowsing> {
        val filteredProducts = localPreferenceDataSource.loadProductBrowsingTags()
            .filter { it.productFormfactorBaseId == productBaseId }
        localPreferenceDataSource.saveFittingFilteredList(filteredProducts)
        return localPreferenceDataSource.getFilteringShapeProducts(filteredProducts)
    }
}
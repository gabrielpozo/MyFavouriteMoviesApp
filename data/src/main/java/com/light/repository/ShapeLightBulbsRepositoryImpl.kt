package com.light.repository

import com.light.domain.ShapeLightBulbRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ShapeBrowsing
import com.light.source.local.LocalPreferenceDataSource


class ShapeLightBulbsRepositoryImpl(
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : ShapeLightBulbRepository {
    override suspend fun getShapeBrowsingProducts(
        productBaseId: Int,
        productBaseName: String
    ): List<ShapeBrowsing> {
        val filteredProducts = localPreferenceDataSource.loadProductBrowsingTags()
            .filter { it.productFormfactorBaseId == productBaseId }
        localPreferenceDataSource.saveSelectedFormFactor(productBaseId)
        localPreferenceDataSource.saveFittingFilteredList(filteredProducts)
        return localPreferenceDataSource.getFilteringShapeProducts(
            filteredProducts,
            productBaseId,
            productBaseName
        )
            .sortedBy { it.order }
    }

    override suspend fun getSavedChoiceBrowsingList(): List<ChoiceBrowsing> =
        localPreferenceDataSource.loadChoiceBrowsingFiltered().sortedBy { it.order }

    override suspend fun getSavedShapeBrowsingList(): List<ShapeBrowsing> =
        localPreferenceDataSource.loadShapeBrowsingFiltered().sortedBy { it.order }


}
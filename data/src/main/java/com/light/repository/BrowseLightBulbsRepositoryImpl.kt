package com.light.repository

import com.light.data.repositoryLightFinderBusinessModel
import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.ProductBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalDbDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteFetchBrowsingSource
import com.light.source.remote.RemoteFetchLegendDataSource


class BrowseLightBulbsRepositoryImpl(
    private val remoteFetchBrowsingSource: RemoteFetchBrowsingSource,
    private val localDbDataSource: LocalDbDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource,
    private val legendRemoteDataSource: RemoteFetchLegendDataSource

) : BrowseLightBulbsRepository {
    override suspend fun getBrowsingProducts(): DataState<List<ProductBrowsing>> =
        repositoryLightFinderBusinessModel(
            shouldDoFetchLegendRequest = localPreferenceDataSource.loadFormFactorLegendTags()
                .isEmpty(),
            legendTagsRemoteRequest = { legendRemoteDataSource.fetchLegendTags() },
            saveLegendRequestOnLocal = { localPreferenceDataSource.saveLegendParsingFilterNames(it) },
            mainRemoteRequest = { remoteFetchBrowsingSource.fetchBrowsingProducts() },
            shouldSaveOnLocalDb = true,
            saveOnDB = { localDbDataSource.saveBrowsingProduct(it) })
}
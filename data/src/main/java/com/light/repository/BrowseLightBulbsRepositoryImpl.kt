package com.light.repository

import com.light.data.repositoryBrowsingBusinessModel
import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.FittingBrowsing
import com.light.domain.state.DataState
import com.light.source.local.LocalDbDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteFetchBrowsingDataSource
import com.light.source.remote.RemoteFetchLegendDataSource


class BrowseLightBulbsRepositoryImpl(
    private val remoteFetchBrowsingSource: RemoteFetchBrowsingDataSource,
    private val localDbDataSource: LocalDbDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource,
    private val legendRemoteDataSource: RemoteFetchLegendDataSource

) : BrowseLightBulbsRepository {
    override suspend fun getBrowsingProducts(): DataState<List<FittingBrowsing>> =
        repositoryBrowsingBusinessModel(
            shouldDoFetchLegendRequest = localPreferenceDataSource.loadFormFactorLegendTags()
                .isEmpty(),
            legendTagsRemoteRequest = { legendRemoteDataSource.fetchLegendTags() },
            saveLegendRequestOnLocal = { localPreferenceDataSource.saveLegendParsingFilterNames(it) },
            mainRemoteRequest = { remoteFetchBrowsingSource.fetchBrowsingProducts() },
            saveOnDB = { localDbDataSource.saveBrowsingProduct(it) },
            legendParsing = { localPreferenceDataSource.getFittingProduct(it) })
}
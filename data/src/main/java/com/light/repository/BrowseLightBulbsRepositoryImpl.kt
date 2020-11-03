package com.light.repository

import com.light.data.repositoryBrowsingBusinessModel
import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteFetchBrowsingDataSource
import com.light.source.remote.RemoteFetchLegendDataSource


class BrowseLightBulbsRepositoryImpl(
    private val remoteFetchBrowsingSource: RemoteFetchBrowsingDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource,
    private val legendRemoteDataSource: RemoteFetchLegendDataSource

) : BrowseLightBulbsRepository {
    override suspend fun getFittingBrowsingProducts(): DataState<List<FormFactorTypeBaseId>> =
        repositoryBrowsingBusinessModel(
            shouldDoFetchLegendRequest = localPreferenceDataSource.loadFormFactorIBaseIdLegendTags()
                .isEmpty(),
            legendTagsRemoteRequest = { legendRemoteDataSource.fetchLegendTags() },
            saveLegendRequestOnLocal = { localPreferenceDataSource.saveLegendParsingFilterNames(it) },
            mainRemoteRequest = { remoteFetchBrowsingSource.fetchBrowsingProducts() },
            saveBrowsingonLocal = { localPreferenceDataSource.saveBrowsingProducts(it) },
            legendParsing = { localPreferenceDataSource.loadFormFactorBasedOnBrowsingProducts(it).sortedBy { it.order } })
}
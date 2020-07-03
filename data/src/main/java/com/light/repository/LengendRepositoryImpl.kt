package com.light.repository

import com.light.data.repositoryLegendHandleSource
import com.light.domain.LegendRepository
import com.light.domain.model.LegendParsing
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteFetchLegendDataSource


class LegendRepositoryImpl(
    private val legendRemoteDataSource: RemoteFetchLegendDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : LegendRepository {
    override suspend fun getLegendTags(): DataState<LegendParsing> =
        repositoryLegendHandleSource(
            remoteSourceRequest = { legendRemoteDataSource.fetchLegendTags() },
            localPreferenceDataSource = { localPreferenceDataSource.saveLegendParsingFilterNames(it) }
        )

}

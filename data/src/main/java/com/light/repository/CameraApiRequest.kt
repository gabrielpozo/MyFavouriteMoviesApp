/*
package com.light.repository

import com.light.data.repositoryCategoryHandleSource
import com.light.data.repositoryCategoryHandleSource2
import com.light.data.repositoryLegendHandleSource
import com.light.domain.LegendRepository
import com.light.domain.model.LegendParsing
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteDataSource
import com.light.source.remote.RemoteFetchLegendDataSource


class CameraApiRequest(
    private val remoteDataSource: RemoteDataSource,
    private val legendRemoteDataSource: RemoteFetchLegendDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource
) {
    suspend fun getLegendTags(base64: String): DataState<List<Message>> =
        repositoryCategoryHandleSource2(
            initialRemoteRequest = legendRemoteDataSource.fetchLegendTags(),
            remoteSourceRequest = { remoteDataSource.fetchMessages(base64) },
            localPreferenceDataSource = { localPreferenceDataSource.saveLegendFilterNames(it) },
            parameterToSave = { it[0].legend }
        )

}
*/

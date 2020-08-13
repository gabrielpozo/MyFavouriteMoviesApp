package com.light.repository


import com.light.data.repositoryLightFinderBusinessModel
import com.light.domain.CategoryRepository
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalMediaDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteDataSource
import com.light.source.remote.RemoteFetchLegendDataSource


class CategoryRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource,
    private val localMediaDataSource: LocalMediaDataSource,
    private val legendRemoteDataSource: RemoteFetchLegendDataSource
) : CategoryRepository {

    override suspend fun getMessage(base64: String?): DataState<List<Message>> =
        repositoryLightFinderBusinessModel(
            shouldDoFetchLegendRequest = localPreferenceDataSource.loadFormFactorLegendTags().isEmpty(),
            legendTagsRemoteRequest = { legendRemoteDataSource.fetchLegendTags() },
            saveLegendRequestOnLocal = { localPreferenceDataSource.saveLegendParsingFilterNames(it) },
            mainRemoteRequest = { remoteDataSource.fetchMessages(base64!!) }
        )

    //TODO(is this this method needed?)
    override suspend fun getFileImagePathEncoded(absolutePath: String): DataState<String> {
        return DataState.Success(localMediaDataSource.getImageFilePath(absolutePath))
    }

}
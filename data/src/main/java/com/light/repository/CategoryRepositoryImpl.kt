package com.light.repository


import com.light.data.repositoryScanningRequest
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
        repositoryScanningRequest(
            initialRemoteRequest = { legendRemoteDataSource.fetchLegendTags() },
            mainRemoteRequest = { remoteDataSource.fetchMessages(base64!!) },
            saveOnLocalDataSourceInitRequest = { localPreferenceDataSource.saveLegendParsingFilterNames(it) },
            localDataSource = { localPreferenceDataSource.saveLegendFilterNames(it) },
            parameterToSave = { it[0].legend },
            shouldDoInitialRequest = localPreferenceDataSource.loadFormFactorLegendTags().isEmpty()

        )

    //TODO(is this this method needed?)
    override suspend fun getFileImagePathEncoded(absolutePath: String): DataState<String> {
        return DataState.Success(localMediaDataSource.getImageFilePath(absolutePath))
    }

}
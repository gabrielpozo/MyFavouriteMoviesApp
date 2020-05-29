package com.light.repository

import com.light.data.repositoryCategoryHandleSource
import com.light.domain.CategoryRepository
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalMediaDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteDataSource


class CategoryRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource,
    private val localMediaDataSource: LocalMediaDataSource
) : CategoryRepository {

    override suspend fun getMessage(base64: String?): DataState<List<Message>> =
        repositoryCategoryHandleSource(
            remoteSourceRequest = { remoteDataSource.fetchMessages(base64!!) },
            localPreferenceDataSource = { localPreferenceDataSource.saveLegendFilterNames(it) },
            parameterToSave = { it[0].legend }
        )

    //TODO(is this this method needed)
    override suspend fun getFileImagePathEncoded(absolutePath: String): DataState<String> {
        return DataState.Success(localMediaDataSource.getImageFilePath(absolutePath))
    }

}
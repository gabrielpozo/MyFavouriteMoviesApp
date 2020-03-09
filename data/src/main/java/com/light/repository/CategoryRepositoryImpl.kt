package com.light.repository

import com.light.data.repositoryHandleSource
import com.light.domain.CategoryRepository
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.local.LocalMediaDataSource
import com.light.source.remote.RemoteDataSource


class CategoryRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localMediaDataSource: LocalMediaDataSource
) : CategoryRepository {

    override suspend fun getMessage(base64: String?): DataState<List<Message>> =
        repositoryHandleSource(remoteSourceRequest = { remoteDataSource.fetchMessages(base64!!) })

    override suspend fun getFileImagePathEncoded(absolutePath: String): DataState<String> {
       return DataState.Success(localMediaDataSource.getImageFilePath(absolutePath))
    }

}
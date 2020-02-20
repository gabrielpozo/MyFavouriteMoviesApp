package com.light.repository

import com.light.data.repositoryHandleSource
import com.light.domain.CategoryRepository
import com.light.domain.model.Message
import com.light.domain.state.DataState
import com.light.source.RemoteDataSource


class CategoryRepositoryImpl(private val remoteDataSource: RemoteDataSource) : CategoryRepository {

    override suspend fun getMessage(base64: String?): DataState<List<Message>> =
        repositoryHandleSource(remoteSourceRequest = { remoteDataSource.fetchMessages(base64!!) })

}
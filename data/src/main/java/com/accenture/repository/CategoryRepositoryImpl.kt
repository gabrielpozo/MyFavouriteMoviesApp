package com.accenture.repository

import com.accenture.data.repositoryHandleSource
import com.accenture.domain.CategoryRepository
import com.accenture.domain.model.Message
import com.accenture.domain.state.DataState
import com.accenture.source.RemoteDataSource


class CategoryRepositoryImpl(private val remoteDataSource: RemoteDataSource) : CategoryRepository {

    override suspend fun getMessage(base64: String): DataState<List<Message>> =
        repositoryHandleSource(remoteSourceRequest = { remoteDataSource.fetchMessages(base64) })

}
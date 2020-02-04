package com.accenture.signify.data.source.remote

import com.accenture.data.Result
import com.accenture.domain.model.Message
import com.accenture.signify.data.mappers.mapServerMessagesToDomain
import com.accenture.source.BaseDataSource
import com.accenture.source.RemoteDataSource

class SignifyRemoteDataSource constructor(private val signifyApiService: SignifyApiService) :
    BaseDataSource(), RemoteDataSource {
    override suspend fun fetchMessages(): Result<List<Message>> =
        getResult(::mapResultToDomainModel) { signifyApiService.fetchMessage() }

    private fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> =
        categoryResult.messageList.map(mapServerMessagesToDomain)
}

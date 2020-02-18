package com.accenture.signify.data.source.remote

import com.accenture.data.Result
import com.accenture.domain.model.Message
import com.accenture.signify.data.mappers.mapServerMessagesToDomain
import com.accenture.source.BaseDataSource
import com.accenture.source.RemoteDataSource
import com.accenture.util.BASE64_PREFIX
import com.google.gson.annotations.SerializedName
import timber.log.Timber


class SignifyRemoteDataSource : BaseDataSource(), RemoteDataSource {

    override suspend fun fetchMessages(base64: String): Result<List<Message>> =
        getResult(::mapResultToDomainModel) {
            MessageRemoteUtil.service.fetchMessageAsync(Image(BASE64_PREFIX + base64))
        }

    private fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> {
        return categoryResult.messageList.map(mapServerMessagesToDomain)
    }
}

data class Image(@SerializedName("image") private val base: String)

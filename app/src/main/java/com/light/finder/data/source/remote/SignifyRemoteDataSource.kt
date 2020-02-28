package com.light.finder.data.source.remote

import com.google.gson.annotations.SerializedName
import com.light.data.Result
import com.light.domain.model.Message
import com.light.finder.data.mappers.mapServerMessagesToDomain
import com.light.source.BaseDataSource
import com.light.source.RemoteDataSource
import com.light.util.BASE64_PREFIX


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

package com.light.finder.data.source.remote

import com.google.gson.annotations.SerializedName
import com.light.data.Result
import com.light.domain.model.Message
import com.light.finder.data.mappers.mapServerMessagesToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.CategoryResultDto
import com.light.finder.data.source.remote.services.MessageRemoteUtil
import com.light.source.remote.RemoteDataSource
import com.light.util.BASE64_PREFIX
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import kotlinx.coroutines.withTimeout


class SignifyRemoteDataSource : BaseDataSource(),
    RemoteDataSource {

    override suspend fun fetchMessages(base64: String): Result<List<Message>> =
        getResult(::mapResultToDomainModel) {
            withTimeout(IMMERSIVE_FLAG_TIMEOUT) {
                MessageRemoteUtil.service.fetchMessageAsync(Image(BASE64_PREFIX + base64))
            }
        }
}


private fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> {
    return categoryResult.messageList?.map(mapServerMessagesToDomain)!!
}


data class Image(@SerializedName("image") private val base: String)

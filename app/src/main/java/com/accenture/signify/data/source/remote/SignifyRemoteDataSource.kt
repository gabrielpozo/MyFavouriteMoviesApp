package com.accenture.signify.data.source.remote

import com.accenture.data.Result
import com.accenture.domain.model.Message
import com.accenture.signify.data.mappers.mapServerMessagesToDomain
import com.accenture.source.BaseDataSource
import com.accenture.source.RemoteDataSource
import com.accenture.util.BASE64
import com.google.gson.annotations.SerializedName


class SignifyRemoteDataSource : BaseDataSource(), RemoteDataSource {

    override suspend fun fetchMessages(): Result<List<Message>> {
        val myData = MyData(BASE64)
        return getResult(::mapResultToDomainModel) {
            MessageRemoteUtil.service.fetchMessageAsync(myData)
        }

    }

    private fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> {
        return mutableListOf(mapServerMessagesToDomain(categoryResult.messageList))
    }
}

data class MyData(@SerializedName("image") private val base: String)

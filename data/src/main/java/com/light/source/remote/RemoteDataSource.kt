package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.Message


interface RemoteDataSource {
    suspend fun fetchMessages(base64: String): Result<List<Message>>
}
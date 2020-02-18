package com.accenture.source

import com.accenture.data.Result
import com.accenture.domain.model.Message


interface RemoteDataSource {
    suspend fun fetchMessages(base64: String): Result<List<Message>>
}
package com.accenture.source

import com.accenture.data.Result
import com.accenture.domain.model.Message


interface RemoteDataSource {
    suspend fun fetchMessages(): Result<Message>
}
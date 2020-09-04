package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.Bearer


interface RemoteFetchAuthDataSource {
    suspend fun fetchBearerToken(): Result<Bearer>
}
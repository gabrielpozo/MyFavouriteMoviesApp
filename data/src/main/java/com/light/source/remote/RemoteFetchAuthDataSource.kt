package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.Credential


interface RemoteFetchAuthDataSource {
    suspend fun fetchBearerToken(): Result<Credential>
}
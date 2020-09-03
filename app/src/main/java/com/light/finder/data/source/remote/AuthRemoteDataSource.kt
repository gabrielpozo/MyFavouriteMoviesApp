package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.Credential
import com.light.finder.data.source.BaseDataSource
import com.light.source.remote.RemoteFetchAuthDataSource

class AuthRemoteDataSource : BaseDataSource(), RemoteFetchAuthDataSource {
    override suspend fun fetchBearerToken(): Result<Credential> {
        TODO("Not yet implemented")
    }

    //todo mapper
}
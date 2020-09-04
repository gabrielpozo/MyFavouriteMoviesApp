package com.light.repository

import com.light.data.repositoryAuthHandleSource
import com.light.domain.AuthRepository
import com.light.domain.model.Bearer
import com.light.domain.state.DataState
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.RemoteFetchAuthDataSource

class AuthRepositoryImpl(
    private val authRemoteDataSource: RemoteFetchAuthDataSource,
    private val localPreferenceDataSource: LocalPreferenceDataSource
) : AuthRepository {
    override suspend fun getBearerToken(): DataState<Bearer> =
        repositoryAuthHandleSource(
            remoteSourceRequest = { authRemoteDataSource.fetchBearerToken() },
            localPreferenceDataSource = { localPreferenceDataSource.saveAccessToken(it) }
        )

}
package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.Bearer
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.BearerResultDto
import com.light.finder.data.source.remote.services.OAuthRemoteUtil
import com.light.source.remote.RemoteFetchAuthDataSource
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import kotlinx.coroutines.withTimeout

class AuthRemoteDataSource : BaseDataSource(), RemoteFetchAuthDataSource {
    override suspend fun fetchBearerToken(): Result<Bearer> =
        getResult(::mapAuthResultToDomainModel) {
            withTimeout(IMMERSIVE_FLAG_TIMEOUT) {
                OAuthRemoteUtil.service.fetchBearerTokenAsync()
            }
        }

    private fun mapAuthResultToDomainModel(bearerResultDto: BearerResultDto): Bearer {
        TODO(" Akis map bearerdto to bearer")
    }
}
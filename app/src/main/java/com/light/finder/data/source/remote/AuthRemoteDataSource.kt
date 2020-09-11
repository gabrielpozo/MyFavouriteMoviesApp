package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.Bearer
import com.light.finder.data.mappers.mapAuthToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.BearerResultDto
import com.light.finder.data.source.remote.services.OAuthRemoteUtil
import com.light.source.remote.RemoteFetchAuthDataSource
import com.light.util.BROWSING_FLAG_TIMEOUT
import kotlinx.coroutines.withTimeout

class AuthRemoteDataSource : BaseDataSource<BearerResultDto, Bearer>(), RemoteFetchAuthDataSource {
    override suspend fun fetchBearerToken(): Result<Bearer> =
        getResult {
            withTimeout(BROWSING_FLAG_TIMEOUT) {
                OAuthRemoteUtil.service.fetchBearerTokenAsync()
            }
        }

    override fun mapResultToDomainModel(dtoObject: BearerResultDto): Bearer {
        return mapAuthToDomain(dtoObject)
    }
}
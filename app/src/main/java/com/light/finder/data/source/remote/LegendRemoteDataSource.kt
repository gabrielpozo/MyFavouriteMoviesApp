package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.LegendParsing
import com.light.finder.data.mappers.mapLegendToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.LegendParsingDto
import com.light.finder.data.source.remote.services.LightFinderOpenRemoteUtil
import com.light.source.remote.RemoteFetchLegendDataSource


class LegendRemoteDataSource : BaseDataSource(), RemoteFetchLegendDataSource {
    override suspend fun fetchLegendTags(): Result<LegendParsing> =
        getResult(::mapLegendResultToDomainModel) {
            LightFinderOpenRemoteUtil.service.fetchLegendTagsAsync()
        }


    private fun mapLegendResultToDomainModel(legendResult: LegendParsingDto): LegendParsing {
        return mapLegendToDomain(legendResult)
    }
}

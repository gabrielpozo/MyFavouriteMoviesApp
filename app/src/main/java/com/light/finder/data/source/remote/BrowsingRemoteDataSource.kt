package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.ProductBrowsing
import com.light.finder.data.mappers.mapProductsBrowsingToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.ProductBrowsingDto
import com.light.finder.data.source.remote.services.LightFinderOpenRemoteUtil
import com.light.source.remote.RemoteFetchBrowsingSource

class BrowsingRemoteDataSource : BaseDataSource(), RemoteFetchBrowsingSource {
    override suspend fun fetchBrowsingProducts(): Result<List<ProductBrowsing>> =
        getResult(::mapLegendResultToDomainModel) {
            LightFinderOpenRemoteUtil.service.fetchBrowsingProductsAsync()
        }


    private fun mapLegendResultToDomainModel(productBrowsingDto: List<ProductBrowsingDto>): List<ProductBrowsing> {
        return mapProductsBrowsingToDomain(productBrowsingDto)
    }
}
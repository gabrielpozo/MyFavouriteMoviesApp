package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.ProductBrowsing
import com.light.finder.data.mappers.mapProductsBrowsingToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.ProductBrowsingDto
import com.light.finder.data.source.remote.dto.ProductBrowsingListDto
import com.light.finder.data.source.remote.services.LightFinderOpenRemoteUtil
import com.light.source.remote.RemoteFetchBrowsingDataSource

class BrowsingRemoteDataSource : BaseDataSource(), RemoteFetchBrowsingDataSource {
    override suspend fun fetchBrowsingProducts(): Result<List<ProductBrowsing>> =
        getResult(::mapLegendResultToDomainModel) {
            LightFinderOpenRemoteUtil.service.fetchBrowsingProductsAsync()
        }


    private fun mapLegendResultToDomainModel(productBrowsingDto: ProductBrowsingListDto): List<ProductBrowsing> {
        return mapProductsBrowsingToDomain(productBrowsingDto)
    }
}
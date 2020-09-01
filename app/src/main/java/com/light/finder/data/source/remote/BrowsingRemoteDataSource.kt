package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.ProductBrowsing
import com.light.finder.data.mappers.mapProductsBrowsingToDomain
import com.light.finder.data.source.BaseCatalogueDataSource
import com.light.finder.data.source.remote.dto.ProductBrowsingListDto
import com.light.finder.data.source.remote.services.LightFinderOpenRemoteUtil
import com.light.source.remote.RemoteFetchBrowsingDataSource
import com.light.util.BROWSING_FLAG_TIMEOUT
import kotlinx.coroutines.withTimeout

class BrowsingRemoteDataSource :
    BaseCatalogueDataSource<ProductBrowsingListDto, List<ProductBrowsing>>(),
    RemoteFetchBrowsingDataSource {
    override suspend fun fetchBrowsingProducts(): Result<List<ProductBrowsing>> =
        getResult {
            withTimeout(BROWSING_FLAG_TIMEOUT) {
                LightFinderOpenRemoteUtil.service.fetchBrowsingProductsAsync()
            }
        }

    override fun mapResultToDomainModel(cartResult: ProductBrowsingListDto): List<ProductBrowsing> {
        return mapProductsBrowsingToDomain(cartResult)
    }
}
package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.ProductBrowsing


interface RemoteFetchBrowsingDataSource {
     suspend fun fetchBrowsingProducts(): Result<List<ProductBrowsing>>
}
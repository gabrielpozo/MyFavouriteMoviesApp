package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.ProductBrowsing


interface RemoteFetchBrowsingSource {
     suspend fun fetchBrowsingProducts(): Result<List<ProductBrowsing>>
}
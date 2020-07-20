package com.light.domain

import com.light.domain.model.ProductBrowsing
import com.light.domain.state.DataState


interface BrowseLightBulbsRepository {
    suspend fun getBrowsingProducts(base64: String?): DataState<List<ProductBrowsing>>
}
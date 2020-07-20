package com.light.source.local

import com.light.domain.model.ProductBrowsing


interface LocalDbDataSource {
    suspend fun getFittingOptions()
    suspend fun getShapeOptions()
    suspend fun saveBrowsingProduct(productsBrowsing: List<ProductBrowsing>)
}
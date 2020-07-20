package com.light.finder.data.source.local.db

import com.light.domain.model.ProductBrowsing
import com.light.finder.data.source.local.db.dao.BrowseDao
import com.light.source.local.LocalDbDataSource


class RoomDataSource(private val browseDao: BrowseDao) : LocalDbDataSource {
    override suspend fun getFittingOptions() {
        TODO("Not yet implemented")
    }

    override suspend fun getShapeOptions() {
        TODO("Not yet implemented")
    }

    override suspend fun saveBrowsingProduct(productsBrowsing: List<ProductBrowsing>) {
        TODO("Not yet implemented")
    }

}
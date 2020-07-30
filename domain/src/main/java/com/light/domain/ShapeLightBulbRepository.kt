package com.light.domain

import com.light.domain.model.ShapeBrowsing

interface ShapeLightBulbRepository {
    suspend fun getShapeBrowsingProducts(productBaseId: Int, productBase: String): List<ShapeBrowsing>
}
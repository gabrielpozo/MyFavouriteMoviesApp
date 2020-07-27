package com.light.domain

import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing


interface ProductBrowsingRepository {
    suspend fun getProductBrowsingRepository(shapeBrowsing: List<ShapeBrowsing>): Message
}
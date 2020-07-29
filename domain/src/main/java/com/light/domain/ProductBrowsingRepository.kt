package com.light.domain

import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.domain.state.DataState


interface ProductBrowsingRepository {
    suspend fun getProductBrowsingRepository(shapeBrowsing: List<ShapeBrowsing>): DataState<Message>
}
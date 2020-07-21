package com.light.domain

import com.light.domain.model.FittingBrowsing
import com.light.domain.state.DataState


interface BrowseLightBulbsRepository {
    suspend fun getBrowsingProducts(): DataState<List<FittingBrowsing>>
}
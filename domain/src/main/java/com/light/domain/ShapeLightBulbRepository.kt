package com.light.domain

import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.state.DataState

interface ShapeLightBulbRepository {
    suspend fun getFittingBrowsingProducts(): DataState<List<FormFactorTypeBaseId>>
}
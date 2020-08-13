package com.light.domain

import com.light.domain.model.LegendParsing
import com.light.domain.state.DataState


interface LegendRepository {
    suspend fun getLegendTags(): DataState<LegendParsing>
}

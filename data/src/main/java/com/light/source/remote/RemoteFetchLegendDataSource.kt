package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.LegendParsing


interface RemoteFetchLegendDataSource {
    suspend fun fetchLegendTags(): Result<LegendParsing>
}
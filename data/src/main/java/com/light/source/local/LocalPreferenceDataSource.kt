package com.light.source.local

import com.light.domain.model.FilterType
import com.light.domain.model.Legend


interface LocalPreferenceDataSource {
    suspend fun saveLegendFilterNames(legend: Legend)
    fun loadLegendCctFilterNames(): List<FilterType>
    fun loadLegendFinishFilterNames(): List<FilterType>
    fun loadLegendFormFactorFilterNames(): List<FilterType>
}
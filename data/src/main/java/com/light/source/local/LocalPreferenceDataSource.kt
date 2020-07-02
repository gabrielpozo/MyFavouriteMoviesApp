package com.light.source.local

import com.light.domain.model.FilterType
import com.light.domain.model.FormFactorType
import com.light.domain.model.Legend
import com.light.domain.model.LegendParsing


interface LocalPreferenceDataSource {
    suspend fun saveLegendFilterNames(legend: Legend)
    suspend fun saveLegendParsingFilterNames(legend: LegendParsing)
    fun loadLegendCctFilterNames(): List<FilterType>
    fun loadLegendFinishFilterNames(): List<FilterType>
    fun loadLegendFormFactorFilterNames(): List<FilterType>
    fun loadFormFactorLegendTags(): List<FormFactorType>

}
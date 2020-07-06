package com.light.source.local

import com.light.domain.model.CctType
import com.light.domain.model.FinishType
import com.light.domain.model.FormFactorType
import com.light.domain.model.LegendParsing


interface LocalPreferenceDataSource {
    suspend fun saveLegendParsingFilterNames(legend: LegendParsing)
    fun loadLegendCctFilterNames(): List<CctType>
    fun loadLegendFinishFilterNames(): List<FinishType>
    fun loadFormFactorLegendTags(): List<FormFactorType>

}
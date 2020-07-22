package com.light.source.local

import com.light.domain.model.*


interface LocalPreferenceDataSource {
    suspend fun saveLegendParsingFilterNames(legend: LegendParsing)
    fun loadLegendCctFilterNames(): List<CctType>
    fun loadLegendFinishFilterNames(): List<FinishType>
    fun loadFormFactorLegendTags(): List<FormFactorType>
    fun loadFormFactorIdLegendTags(): List<FormFactorTypeId>
    fun loadFormFactorIBaseIdLegendTags():List<FormFactorTypeBaseId>
    fun getFittingProducts(productsBrowsing: List<ProductBrowsing>): List<FittingBrowsing>
    fun saveBrowsingProducts(productsBrowsing: List<ProductBrowsing>)
}
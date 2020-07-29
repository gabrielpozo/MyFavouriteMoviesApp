package com.light.source.local

import com.light.domain.model.*


interface LocalPreferenceDataSource {
    suspend fun saveLegendParsingFilterNames(legend: LegendParsing)
    fun loadLegendCctFilterNames(): List<CctType>
    fun loadLegendFinishFilterNames(): List<FinishType>
    fun loadFormFactorLegendTags(): List<FormFactorType>
    fun loadFormFactorIdLegendTags(): List<FormFactorTypeId>
    fun loadFormFactorIBaseIdLegendTags(): List<FormFactorTypeBaseId>
    fun loadProductCategoryName(): List<ProductCategoryName>
    fun loadProductBrowsingTags(): List<ProductBrowsing>
    fun loadProductBrowsingFiltered(): List<ProductBrowsing>
    fun saveBrowsingProducts(productsBrowsing: List<ProductBrowsing>)
    fun saveFittingFilteredList(productsFilteredBrowsing: List<ProductBrowsing>)
    fun getFilteringShapeProducts(
        productFilteredBrowseList: List<ProductBrowsing>,
        productBaseId: Int
    ): List<ShapeBrowsing>

    fun getFilteredProductsMessage(shapeBrowsingList: List<ShapeBrowsing>): Message
    fun getAllProductsMessage(): Message
}
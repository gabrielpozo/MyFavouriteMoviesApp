package com.light.source.local

import com.light.domain.model.*


interface LocalPreferenceDataSource {
    suspend fun saveLegendParsingFilterNames(legend: LegendParsing)
    fun loadLegendCctFilterNames(): List<CctType>
    fun loadLegendFinishFilterNames(): List<FinishType>
    fun loadFormFactorLegendTags(): List<FormFactorType>
    fun loadFormFactorIdLegendTags(): List<FormFactorTypeId>
    fun loadLegendConnectivityNames(): List<ProductConnectivity>
    fun loadFormFactorIBaseIdLegendTags(): List<FormFactorTypeBaseId>
    fun loadFormFactorBasedOnBrowsingProducts(productsFilteredBrowsing: List<ProductBrowsing>): List<FormFactorTypeBaseId>
    fun loadProductCategoryName(): List<ProductCategoryName>
    fun loadProductBrowsingTags(): List<ProductBrowsing>
    fun loadProductBrowsingFiltered(): List<ProductBrowsing>
    fun loadProductShapeBrowsingFiltered(): List<ProductBrowsing>
    fun loadShapeBrowsingFiltered(): List<ShapeBrowsing>
    fun loadChoiceBrowsingFiltered(): List<ChoiceBrowsing>
    fun loadFormFactorBrowsingFiltered(): List<FormFactorTypeBaseId>
    fun getProductBaseId(): Int
    fun saveBrowsingProducts(productsBrowsing: List<ProductBrowsing>)
    fun saveProductBrowsingFilteredList(productsFilteredBrowsing: List<ProductBrowsing>)
    fun saveBrowsingShapeFilteredList(browsingList: List<ShapeBrowsing>)
    fun saveFittingFilteredList(productsFilteredBrowsing: List<ProductBrowsing>)
    fun saveChoiceCategories(choiceBrowsingList: List<ChoiceBrowsing>)
    fun saveFormFactorFilteredList(filterFilteringList: List<FormFactorTypeBaseId>)
    fun saveSelectedFormFactor(baseId: Int)
    fun getFilteringShapeProducts(
        productFilteredBrowseList: List<ProductBrowsing>,
        productBaseId: Int,
        productBaseName: String
    ): List<ShapeBrowsing>

    fun getFilteredProductsMessage(shapeBrowsingList: List<ShapeBrowsing>): Message
    fun getFilteredProductsMessageFromChoice(shapeBrowsingList: List<ChoiceBrowsing>): Message
    fun getShapeFilteredList(shapeBrowsingList: List<ShapeBrowsing>): List<ProductBrowsing>
    fun getAllProductsMessage(baseIdFitting: String): Message
    fun disclaimerAccepted(confirmed: Boolean)
    fun isDisclaimerAccepted(): Boolean
}
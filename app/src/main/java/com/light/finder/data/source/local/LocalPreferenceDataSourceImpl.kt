package com.light.finder.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.light.domain.model.*
import com.light.domain.toKey
import com.light.finder.data.mappers.mapBrowsingProductToMessageDomain
import com.light.finder.extensions.fromJson
import com.light.source.local.LocalPreferenceDataSource


class LocalPreferenceDataSourceImpl(private val context: Context) :
    LocalPreferenceDataSource {

    companion object {
        private const val PREF_NAME = "sharedPrefLegend"
        private const val CCT_LEGEND = "cctColorLegend"
        private const val PRODUCT_FINISH_LEGEND = "productFinishLegend"
        private const val FORM_FACTOR_LEGEND = "productFormFactorLegend"
        private const val FORM_FACTOR_LEGEND_ID = "productFormFactorIdLegend"
        private const val PRODUCT_CONNECTIVITY_LEGEND = "productConnectivityLegend"
        private const val FORM_FACTOR_LEGEND_BASE_ID = "productFormFactorBaseIdLegend"
        private const val PRODUCTS_BROWSING_BASE = "productsBrowsingBase"
        private const val PRODUCTS_FILTERED_PRODUCT_BROWSING = "filteredProductsBrowsing"
        private const val PRODUCT_CATEGORY_NAME = "productCategoryName"
        private const val PRODUCTS_FILTERED_SHAPED_BROWSING = "products_filtered_shaped_browsing"
        private const val DISCLAIMER_TEXT = "disclaimerText"
        private const val ACCESS_TOKEN = "accessToken"
        private const val TOKEN_TYPE = "tokenType"


    }

    private val PRIVATE_MODE = 0
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = pref.edit().also { it.apply() }


    override suspend fun saveLegendParsingFilterNames(legend: LegendParsing) {
        editor.putString(CCT_LEGEND, Gson().toJson(legend.legend.cctType))
        editor.putString(PRODUCT_FINISH_LEGEND, Gson().toJson(legend.legend.finishType))
        editor.putString(FORM_FACTOR_LEGEND, Gson().toJson(legend.legend.productFormFactorType))
            .commit()
        editor.putString(FORM_FACTOR_LEGEND_ID, Gson().toJson(legend.legend.formfactorTypeId))
            .commit()
        editor.putString(
            PRODUCT_CONNECTIVITY_LEGEND,
            Gson().toJson(legend.legend.productConnectivity)
        )
            .commit()
        editor.putString(
            FORM_FACTOR_LEGEND_BASE_ID,
            Gson().toJson(legend.legend.formfactorTypeBaseId)
        ).commit()

        editor.putString(
            PRODUCT_CATEGORY_NAME,
            Gson().toJson(legend.legend.productCategoryName)
        ).commit()
    }

    override fun disclaimerAccepted(confirmed: Boolean) {
        editor.putBoolean(DISCLAIMER_TEXT, confirmed)
            .commit()
    }

    override fun isDisclaimerAccepted(): Boolean =
        pref.getBoolean(DISCLAIMER_TEXT, false)

    override fun saveAccessToken(credentials: Bearer) {
        editor.putString(ACCESS_TOKEN, credentials.accessToken).commit()
    }

    override fun saveTokenType(credentials: Bearer) {
        editor.putString(TOKEN_TYPE, credentials.tokenType).commit()
    }

    override fun loadAccessToken(): String {
        return pref.getString(ACCESS_TOKEN, null) ?: ""
    }

    override fun loadTokenType(): String {
        return pref.getString(TOKEN_TYPE, null) ?: ""
    }

    override fun saveBrowsingProducts(productsBrowsing: List<ProductBrowsing>) {
        editor.putString(
            PRODUCTS_BROWSING_BASE,
            Gson().toJson(productsBrowsing)
        ).commit()
    }

    override fun loadLegendCctFilterNames(): List<CctType> =
        Gson().fromJson(pref.getString(CCT_LEGEND, null) ?: "")

    override fun loadLegendFinishFilterNames(): List<FinishType> =
        Gson().fromJson(pref.getString(PRODUCT_FINISH_LEGEND, null) ?: "")

    override fun loadFormFactorLegendTags(): List<FormFactorType> = Gson().fromJson(
        pref.getString(FORM_FACTOR_LEGEND, null) ?: emptyList<FormFactorType>().toString()
    )

    override fun loadFormFactorIdLegendTags(): List<FormFactorTypeId> = Gson().fromJson(
        pref.getString(FORM_FACTOR_LEGEND_ID, null) ?: emptyList<FormFactorTypeId>().toString()
    )

    override fun loadLegendConnectivityNames(): List<ProductConnectivity> =
        Gson().fromJson(pref.getString(PRODUCT_CONNECTIVITY_LEGEND, null) ?: "")

    override fun loadFormFactorIBaseIdLegendTags(): List<FormFactorTypeBaseId> = Gson().fromJson(
        pref.getString(FORM_FACTOR_LEGEND_BASE_ID, null)
            ?: emptyList<FormFactorTypeBaseId>().toString()
    )

    override fun loadProductCategoryName(): List<ProductCategoryName> = Gson().fromJson(
        pref.getString(PRODUCT_CATEGORY_NAME, null)
            ?: emptyList<ProductBrowsing>().toString()
    )

    override fun loadProductBrowsingTags(): List<ProductBrowsing> = Gson().fromJson(
        pref.getString(PRODUCTS_BROWSING_BASE, null) ?: emptyList<ProductBrowsing>().toString()
    )

    override fun loadProductBrowsingFiltered(): List<ProductBrowsing> = Gson().fromJson(
        pref.getString(PRODUCTS_FILTERED_PRODUCT_BROWSING, null)
            ?: emptyList<ProductBrowsing>().toString()
    )

    override fun loadProductShapeBrowsingFiltered(): List<ProductBrowsing> = Gson().fromJson(
        pref.getString(PRODUCTS_FILTERED_SHAPED_BROWSING, null)
            ?: emptyList<ProductBrowsing>().toString()
    )


    override fun getAllProductsMessage(baseNameFitting: String): Message {
        val productsFiltered = loadProductBrowsingFiltered()
        return mapBrowsingProductToMessageDomain(
            baseNameFitting,
            loadProductCategoryName(),
            loadFormFactorLegendTags(),
            productsFiltered.groupBy { it.toKey() })
    }


    override fun saveFittingFilteredList(productsFilteredBrowsing: List<ProductBrowsing>) {
        editor.putString(
            PRODUCTS_FILTERED_PRODUCT_BROWSING,
            Gson().toJson(productsFilteredBrowsing)
        ).commit()
    }

    override fun saveShapeFilteredList(productsFilteredBrowsing: List<ProductBrowsing>) {
        editor.putString(
            PRODUCTS_FILTERED_SHAPED_BROWSING,
            Gson().toJson(productsFilteredBrowsing)
        ).commit()
    }



    override fun getFilteringShapeProducts(
        productFilteredBrowseList: List<ProductBrowsing>,
        baseFittingId: Int,
        productBaseName: String
    ): List<ShapeBrowsing> {
        val shapesToDisplay = arrayListOf<ShapeBrowsing>()
        val allShapes = loadFormFactorLegendTags()
        allShapes.forEach { shape ->
            shapesToDisplay.add(
                ShapeBrowsing(
                    shape.id,
                    shape.name,
                    shape.image,
                    shape.order.toInt(),
                    productFilteredBrowseList.filter { productBrowse ->
                        shape.id == productBrowse.productFormfactorTypeCode
                    }.groupBy { it.toKey() }.size,
                    baseNameFitting = productBaseName,
                    baseIdFitting = baseFittingId
                )
            )
        }

/*        shapesToDisplay.add(
            ShapeBrowsing(
                id = 15,
                name = "Ega",
                image = "",
                order = 19,
                subtitleCount = 9,
                baseIdFitting = 2,
                baseNameFitting = "lol",
                isSelected = false))

        shapesToDisplay.add(
            ShapeBrowsing(
                id = 16,
                name = "Alignment",
                image = "",
                order = 21,
                subtitleCount = 4,
                baseIdFitting = 2,
                baseNameFitting = "lolment",
                isSelected = false))*/


                return shapesToDisplay
    }


    override fun getFilteredProductsMessage(shapeBrowsingList: List<ShapeBrowsing>): Message {
        return mapBrowsingProductToMessageDomain(
            shapeBrowsingList[0].baseNameFitting,
            loadProductCategoryName(),
            loadFormFactorLegendTags(),
            getShapeFilteredList(shapeBrowsingList).groupBy { it.toKey() })
    }

    override fun getFilteredProductsMessageFromChoice(choiceBrowsingList: List<ChoiceBrowsing>): Message {
        return mapBrowsingProductToMessageDomain(
            choiceBrowsingList[0].baseNameFitting,
            loadProductCategoryName(),
            loadFormFactorLegendTags(),
            getChoiceFilteredList(choiceBrowsingList).groupBy { it.toKey() })
    }


    private fun getChoiceFilteredList(choiceBrowsingList: List<ChoiceBrowsing>): List<ProductBrowsing> {
        val browsedFilteredList = loadProductShapeBrowsingFiltered()
        val browsedShapeFilteredList = mutableListOf<ProductBrowsing>()
        choiceBrowsingList.forEach { choiceBrowse ->
            if (choiceBrowse.isSelected) {
                browsedShapeFilteredList.addAll(browsedFilteredList.filter { productBrowse ->
                    choiceBrowse.id == productBrowse.productCategoryCode
                })
            }
        }

        return if (browsedShapeFilteredList.isNotEmpty()) browsedShapeFilteredList else browsedFilteredList
    }

    override fun  getShapeFilteredList(shapeBrowsingList: List<ShapeBrowsing>): List<ProductBrowsing> {
        val browsedFilteredList = loadProductBrowsingFiltered()
        val browsedShapeFilteredList = mutableListOf<ProductBrowsing>()
        shapeBrowsingList.forEach { shapeBrowse ->
            if (shapeBrowse.isSelected) {
                browsedShapeFilteredList.addAll(browsedFilteredList.filter { productBrowse ->
                    shapeBrowse.id == productBrowse.productFormfactorTypeCode
                })
            }
        }

        return browsedShapeFilteredList

    }
}


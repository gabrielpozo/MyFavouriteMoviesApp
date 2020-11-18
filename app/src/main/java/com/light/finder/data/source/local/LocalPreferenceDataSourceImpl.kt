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
        private const val SHAPE_BROWSING_LIST = "shapeBrowsingList"
        private const val PRODUCTS_FILTERED_PRODUCT_BROWSING = "filteredProductsBrowsing"
        private const val PRODUCT_CATEGORY_NAME = "productCategoryName"
        private const val PRODUCTS_FILTERED_SHAPED_BROWSING = "products_filtered_shaped_browsing"
        private const val CHOICE_BROWSING_LIST = "choiceBrowsingList"
        private const val FORM_FACTOR_BROWSING_LIST = "formFactorBrowsingList"
        private const val DISCLAIMER_TEXT = "disclaimerText"
        private const val BASE_ID = "BASE_ID"
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


    override fun saveBrowsingProducts(productsBrowsing: List<ProductBrowsing>) {
        editor.putString(
            PRODUCTS_BROWSING_BASE,
            Gson().toJson(productsBrowsing)
        ).commit()
    }

    override fun saveBrowsingShapeFilteredList(browsingList: List<ShapeBrowsing>) {
        editor.putString(
            SHAPE_BROWSING_LIST,
            Gson().toJson(browsingList)
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

    override fun loadFormFactorBasedOnBrowsingProducts(productsFilteredBrowsing: List<ProductBrowsing>): List<FormFactorTypeBaseId> =
        loadFormFactorIBaseIdLegendTags().filter { formFactor ->
            productsFilteredBrowsing.find { it.productFormfactorBaseId == formFactor.id } != null
        }

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

    override fun loadShapeBrowsingFiltered(): List<ShapeBrowsing> = Gson().fromJson(
        pref.getString(SHAPE_BROWSING_LIST, null)
            ?: emptyList<ShapeBrowsing>().toString()
    )

    override fun loadChoiceBrowsingFiltered(): List<ChoiceBrowsing> = Gson().fromJson(
        pref.getString(CHOICE_BROWSING_LIST, null)
            ?: emptyList<ChoiceBrowsing>().toString()
    )

    override fun loadFormFactorBrowsingFiltered(): List<FormFactorTypeBaseId> = Gson().fromJson(
        pref.getString(FORM_FACTOR_BROWSING_LIST, null)
            ?: emptyList<FormFactorTypeBaseId>().toString()
    )

    override fun getProductBaseId(): Int =
        pref.getInt(BASE_ID, -1)


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

    override fun saveProductBrowsingFilteredList(productsFilteredBrowsing: List<ProductBrowsing>) {
        editor.putString(
            PRODUCTS_FILTERED_SHAPED_BROWSING,
            Gson().toJson(productsFilteredBrowsing)
        ).commit()
    }

    override fun saveChoiceCategories(choiceBrowsingList: List<ChoiceBrowsing>) {
        editor.putString(
            CHOICE_BROWSING_LIST,
            Gson().toJson(choiceBrowsingList)
        ).commit()
    }

    override fun saveFormFactorFilteredList(filterFilteringList: List<FormFactorTypeBaseId>) {
        editor.putString(
            FORM_FACTOR_BROWSING_LIST,
            Gson().toJson(filterFilteringList)
        ).commit()
    }

    override fun saveSelectedFormFactor(baseId: Int) {
        editor.putInt(
            BASE_ID,
            baseId
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

    override fun getShapeFilteredList(shapeBrowsingList: List<ShapeBrowsing>): List<ProductBrowsing> {
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


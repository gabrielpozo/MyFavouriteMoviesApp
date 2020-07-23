package com.light.finder.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.light.domain.model.*

import com.light.source.local.LocalPreferenceDataSource


class LocalPreferenceDataSourceImpl(private val context: Context) :
    LocalPreferenceDataSource {

    companion object {
        private const val PREF_NAME = "sharedPrefLegend"
        private const val CCT_LEGEND = "cctColorLegend"
        private const val PRODUCT_FINISH_LEGEND = "productFinishLegend"
        private const val FORM_FACTOR_LEGEND = "productFormFactorLegend"
        private const val FORM_FACTOR_LEGEND_ID = "productFormFactorIdLegend"
        private const val FORM_FACTOR_LEGEND_BASE_ID = "productFormFactorBaseIdLegend"
        private const val PRODUCTS_BROWSING_BASE = "productsBrowsingBase"
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
            FORM_FACTOR_LEGEND_BASE_ID,
            Gson().toJson(legend.legend.formfactorTypeBaseId)
        ).commit()
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

    override fun loadFormFactorIBaseIdLegendTags(): List<FormFactorTypeBaseId> = Gson().fromJson(
        pref.getString(FORM_FACTOR_LEGEND_BASE_ID, null)
            ?: emptyList<FormFactorTypeBaseId>().toString()
    )

    override fun loadProductBrowsingTags(): List<ProductBrowsing> = Gson().fromJson(
        pref.getString(PRODUCTS_BROWSING_BASE, null) ?: emptyList<ProductBrowsing>().toString()
    )


    override fun fittingFilteringProducts(productFilteredBrowseList: List<ProductBrowsing>): List<ShapeBrowsing> {
        val shapesToDisplay = arrayListOf<ShapeBrowsing>()
        val allShapes = loadFormFactorLegendTags()
        allShapes.forEach { formFactor ->
            shapesToDisplay.add(
                ShapeBrowsing(
                    formFactor.id,
                    formFactor.name,
                    formFactor.image,
                    formFactor.order.toInt(),
                    productFilteredBrowseList.count { formFactor.id == it.productFormfactorTypeCode }
                )
            )
        }
        return shapesToDisplay
    }
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)
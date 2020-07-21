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

    override fun getFittingProduct(productsBrowsing: List<ProductBrowsing>): List<FittingBrowsing> {
        val fittingList = hashSetOf<FittingBrowsing>()
        val formFactorBaseIdList = Gson().fromJson<List<FormFactorTypeBaseId>>(
            pref.getString(FORM_FACTOR_LEGEND_BASE_ID, null)
                ?: emptyList<FormFactorTypeBaseId>().toString()
        )
        productsBrowsing.map { productsBrowsing ->
            val formFactorTypeBase =
                formFactorBaseIdList.find { productsBrowsing.productFormfactorBaseId == it.id }
            if (formFactorTypeBase != null) {
                fittingList.add(
                    FittingBrowsing(
                        id = formFactorTypeBase.id,
                        name = formFactorTypeBase.name,
                        image = formFactorTypeBase.image,
                        order = formFactorTypeBase.order
                    )
                )

            }
        }
        return fittingList.toList().sortedBy { it.order }
    }

}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)
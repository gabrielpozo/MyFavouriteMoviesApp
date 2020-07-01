package com.light.finder.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.light.domain.model.FilterType
import com.light.domain.model.FormFactorType
import com.light.domain.model.Legend
import com.light.domain.model.LegendParsing

import com.light.source.local.LocalPreferenceDataSource


class LocalPreferenceDataSourceImpl(private val context: Context) :
    LocalPreferenceDataSource {

    companion object {
        private const val PREF_NAME = "sharedPrefLegend"
        private const val PRODUCT_CCT = "cctColor"
        private const val PRODUCT_FINISH = "productFinish"
        private const val FORM_FACTOR = "productFormFactor"
        private const val FORM_FACTOR_LEGEND = "productFormFactorLegend"

    }

    private val PRIVATE_MODE = 0
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = pref.edit().also { it.apply() }

    override suspend fun saveLegendFilterNames(legend: Legend) {
        editor.putString(PRODUCT_CCT, Gson().toJson(legend.cctFilter))
        editor.putString(PRODUCT_FINISH, Gson().toJson(legend.finishFilter))
        editor.putString(FORM_FACTOR, Gson().toJson(legend.lightShapeFilter))

        editor.commit()
    }

    override suspend fun saveLegendParsingFilterNames(legend: LegendParsing) {
        editor.putString(FORM_FACTOR_LEGEND, Gson().toJson(legend.legend.productFormFactorType)).commit()
    }

    override fun loadLegendCctFilterNames(): List<FilterType> =
        Gson().fromJson(pref.getString(PRODUCT_CCT, null) ?: "")

    override fun loadLegendFinishFilterNames(): List<FilterType> =
        Gson().fromJson(pref.getString(PRODUCT_FINISH, null) ?: "")

    //TODO("this method will be removed at some point")
    override fun loadLegendFormFactorFilterNames(): List<FilterType> =
        Gson().fromJson(pref.getString(FORM_FACTOR, null) ?: "")

    override fun loadFormFactorLegendTags(): List<FormFactorType> =
        Gson().fromJson(
            pref.getString(FORM_FACTOR_LEGEND, null) ?: emptyList<FormFactorType>().toString()
        )
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)
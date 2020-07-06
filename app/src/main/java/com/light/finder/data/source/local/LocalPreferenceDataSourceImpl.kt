package com.light.finder.data.source.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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

    }

    private val PRIVATE_MODE = 0
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = pref.edit().also { it.apply() }


    override suspend fun saveLegendParsingFilterNames(legend: LegendParsing) {
        Log.d("Gabriel","is saving legend: ${legend.legend}")
        editor.putString(CCT_LEGEND, Gson().toJson(legend.legend.cctType))
        editor.putString(PRODUCT_FINISH_LEGEND, Gson().toJson(legend.legend.finishType))
        editor.putString(FORM_FACTOR_LEGEND, Gson().toJson(legend.legend.productFormFactorType))
            .commit()
    }

    override fun loadLegendCctFilterNames(): List<CctType> =
        Gson().fromJson(pref.getString(CCT_LEGEND, null) ?: "")

    override fun loadLegendFinishFilterNames(): List<FinishType> =
        Gson().fromJson(pref.getString(PRODUCT_FINISH_LEGEND, null) ?: "")

    override fun loadFormFactorLegendTags(): List<FormFactorType> = Gson().fromJson(
        pref.getString(FORM_FACTOR_LEGEND, null) ?: emptyList<FormFactorType>().toString()
    )
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)
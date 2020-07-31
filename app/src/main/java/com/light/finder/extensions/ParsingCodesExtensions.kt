package com.light.finder.extensions

import android.content.Context
import android.util.Log
import com.light.domain.model.*
import com.light.finder.R
import com.light.finder.data.source.remote.reports.CrashlyticsException


const val COLOR_LEGEND_TAG = "product_cct_code"
const val FINISH_LEGEND_TAG = "product_finish_code"
const val FORM_FACTOR_LEGEND_TAG = "product_formfactor_type_code"
const val CONNECTIVITY_LEGEND_TAG = "product_connection_code"

//TODO("this method will be removed at some point")
fun getLegendCctTagPref(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<CctType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.name

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendCctTagPrefIcon(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<CctType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.bigIcon

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}


fun getLegendFinishTagPref(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FinishType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.name

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendConnectivityTagPref(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<ProductConnectivity>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.name

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendArTypeTagPref(
    code: Int,
    filterTypeList: List<CctType>

): Boolean {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return productColor?.arType == 1
}


fun getFormFactorIdTagName(
    shapeIdentified: String,
    formFactoridTypeList: List<FormFactorTypeId>

): String {
    Log.d("Gabriel","FormFactor List : $formFactoridTypeList")
    val formFactorTypeId = formFactoridTypeList.find {
        it.name == shapeIdentified
    }
    return formFactorTypeId?.productFormFactorType ?: ""
}


fun getLegendFormFactorTag(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FormFactorType>,
    legendTag: String
): String {
    val formFactorType = filterTypeList.find {
        it.id == code
    }
    return if (formFactorType != null) {
        formFactorType.name

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }

}

fun getOrderColorVariation(
    code: Int,
    filterTypeList: List<FinishType>
): Int {
    val productFinish = filterTypeList.find {
        it.id == code
    }
    return productFinish?.order?.toInt() ?: -1
}


fun getLegendFinishTagPrefImage(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FinishType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.image

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendConnectivityTagPrefImage(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<ProductConnectivity>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.image

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendCctTagPrefSmallIcon(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<CctType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return if (productColor != null) {
        productColor.smallIcon

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun getLegendFormFactorTagPrefSmallIcon(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FinishType>,
    legendTag: String
): String {
    val productFormFactor = filterTypeList.find {
        it.id == code
    }
    return if (productFormFactor != null) {
        productFormFactor.image

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}


fun getLegendTagPrefFormFactor(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FormFactorType>,
    legendTag: String
): String {
    val formFactorType = filterTypeList.find {
        it.id == code
    }
    return if (formFactorType != null) {
        formFactorType.name

    } else {
        if (logError) CrashlyticsException(422, legendTag, code).logException()
        if (!isForDetailScreen) {
            code.toString()
        } else {
            ""
        }
    }
}

fun checkCategoryColorCodesAreValid(categoryCodes: List<Int>) {
    categoryCodes.forEach { code ->
        if (code > 4 || code < 1) {
            CrashlyticsException(422, "category_filter_cct_code", code).logException()
        }
    }
}

fun checkCategoryFinishCodesAreValid(finishCodes: List<Int>) {
    finishCodes.forEach { code ->
        if (code > 4 || code < 1) {
            CrashlyticsException(422, "category_filter_finish_code", code).logException()
        }
    }
}


fun List<FilterVariationCF>.sortFinishByOrderField(filterFinishList: List<FinishType>): List<FilterVariationCF> {
    val orderedList = map {
        it.order = getOrderFinish(it.codeFilter, filterFinishList)
        it
    }

    return orderedList.sortedBy { it.order }
}

fun List<FilterVariationCF>.sortColorByOrderField(filterColorList: List<CctType>): List<FilterVariationCF> {
    val orderedList = map {
        it.order = getOrderColor(it.codeFilter, filterColorList)
        it
    }

    return orderedList.sortedBy { it.order }
}

fun List<Int>.sortSmallColorByOrderField(filterColorList: List<CctType>): List<ColorOrderList> {
    val orderedColors = arrayListOf<ColorOrderList>()
    forEach { orderedColors.add(ColorOrderList(it, getOrderColor(it, filterColorList))) }
    orderedColors.sortBy { it.order }
    return orderedColors
}

fun List<Int>.sortSmallFinishByOrderField(filterFinishList: List<FinishType>): List<FinishOrderList> {
    val orderedFinish = arrayListOf<FinishOrderList>()
    forEach { orderedFinish.add(FinishOrderList(it, getOrderFinish(it, filterFinishList))) }
    orderedFinish.sortBy { it.order }
    return orderedFinish
}


fun getOrderColor(
    code: Int,
    filterTypeList: List<CctType>
): Int {
    val productColor = filterTypeList.find {
        it.id == code
    }
    return productColor?.order ?: -1
}

fun getOrderFinish(
    code: Int,
    filterTypeList: List<FinishType>
): Int {
    val productFinish = filterTypeList.find {
        it.id == code
    }
    return productFinish?.order?.toInt() ?: -1
}


fun Context.getColorDrawable(colorCode: Int): Int = when (colorCode) {
    1 -> {
        R.drawable.ic_warm
    }
    2 -> {
        R.drawable.ic_warm_white
    }
    3 -> {
        R.drawable.ic_soft_white
    }
    4 -> {
        R.drawable.cool_white
    }
    5 -> {
        R.drawable.daylight
    }
    6 -> {
        R.drawable.tunnable_white
    }
    7 -> {
        R.drawable.full_color_range
    }
    else -> {
        R.drawable.ic_holder
    }
}

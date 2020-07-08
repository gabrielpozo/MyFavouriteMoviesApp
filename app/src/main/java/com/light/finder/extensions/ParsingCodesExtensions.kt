package com.light.finder.extensions

import android.content.Context
import android.view.View
import com.light.domain.model.CctType
import com.light.domain.model.FinishType
import com.light.domain.model.FormFactorType
import com.light.finder.R
import com.light.finder.data.source.remote.reports.CrashlyticsException
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*


const val COLOR_LEGEND_TAG = "product_cct_code"
const val FINISH_LEGEND_TAG = "product_finish_code"
const val FORM_FACTOR_LEGEND_TAG = "product_formfactor_type_code"

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

fun View.setFinishVariation(finishCode: Int) {
    when (finishCode) {
        1 -> {
            imageFilterCover.setBackgroundResource(R.drawable.clear)
        }
        2 -> {
            imageFilterCover.setBackgroundResource(R.drawable.frosted)
        }
        3 -> {
            imageFilterCover.setBackgroundResource(R.drawable.variation_finish_amber)
        }
        else -> {
            imageFilterCover.setBackgroundResource(R.drawable.ic_placeholder_variation)
        }
    }
}
package com.light.finder.extensions

import android.content.Context
import android.view.View
import com.light.domain.model.FilterType
import com.light.finder.R
import com.light.finder.data.source.remote.reports.CrashlyticsException
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*


const val COLOR_LEGEND_TAG = "product_cct_code"
const val FINISH_LEGEND_TAG = "product_finish_code"
const val FORM_FACTOR_LEGEND_TAG = "product_formfactor_type_code"


fun getLegendTagPref(
    code: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false,
    filterTypeList: List<FilterType>,
    legendTag: String
): String {
    val productColor = filterTypeList.find {
        it.id == code.toString()
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

fun String.dropFirstAndLastCharacter(): String {
    val removeFirst = removePrefix(" -")
    return when {
        removeFirst.isEmpty() -> {
            removeFirst
        }
        removeFirst.takeLast(2) == "- " -> {
            removeFirst.substring(0, removeFirst.length - 2)
        }
        else -> {
            removeFirst
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

fun View.setColorVariation(colorCode: Int) {
    when (colorCode) {
        1 -> {
            imageFilterCover.setBackgroundResource(R.drawable.warm)
        }
        2 -> {
            imageFilterCover.setBackgroundResource(R.drawable.warm_white)
        }
        3 -> {
            imageFilterCover.setBackgroundResource(R.drawable.variation_color_soft_white)
        }
        4 -> {
            imageFilterCover.setBackgroundResource(R.drawable.cool_white_variation)
        }
        5 -> {
            imageFilterCover.setBackgroundResource(R.drawable.daylight_variation)
        }
        6 -> {
            imageFilterCover.setBackgroundResource(R.drawable.tunable)
        }
        7 -> {
            imageFilterCover.setBackgroundResource(R.drawable.full_color)
        }
        else -> {
            imageFilterCover.setBackgroundResource(R.drawable.ic_placeholder_variation)
        }
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
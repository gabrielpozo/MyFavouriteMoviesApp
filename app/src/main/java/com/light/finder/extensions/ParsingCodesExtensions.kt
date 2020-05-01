package com.light.finder.extensions

import android.content.Context
import android.view.View
import com.light.finder.R
import com.light.finder.data.source.remote.reports.CrashlyticsException
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

fun Context.getColorName(
    colorCode: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false
): String = when (colorCode) {
    1 -> {
        getString(R.string.warm)
    }
    2 -> {
        getString(R.string.white_warm)
    }
    3 -> {
        getString(R.string.cool_white)
    }
    4 -> {
        getString(R.string.daylight)
    }
    else -> {
        if (logError) CrashlyticsException(422, "product_cct_code", colorCode).logException()
        if (!isForDetailScreen) {
            colorCode.toString()
        } else {
            ""
        }
    }
}


fun Context.getformFactortType(factorTypeCode: Int): String = when (factorTypeCode) {
    1 -> "Bulb"
    2 -> "Reflector"
    3 -> "Coil"
    4 -> "Tube"
    else -> {
        CrashlyticsException(422, "product_formfactor_type_code", factorTypeCode).logException()
        ""
    }
}

fun Context.getFinishName(
    finishCode: Int,
    logError: Boolean = false,
    isForDetailScreen: Boolean = false
): String = when (finishCode) {
    1 -> if (!isForDetailScreen) {
        getString(R.string.clear)
    } else {
        getString(R.string.clear) + " " + getString(R.string.finish)
    }
    2 -> if (!isForDetailScreen) {
        getString(R.string.frosted)
    } else {
        getString(R.string.frosted) + " " + getString(R.string.finish)
    }
    else -> {
        if (logError) CrashlyticsException(422, "product_finish_code", finishCode).logException()
        if (!isForDetailScreen) {
            finishCode.toString()
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

fun String.dropLastCharacter(): String {
    return substring(0, length - 1)
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
        R.drawable.ic_cool_white
    }
    4 -> {
        R.drawable.ic_daylight
    }

    else -> 0
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
            imageFilterCover.setBackgroundResource(R.drawable.cool_white)
        }

        4 -> {
            imageFilterCover.setBackgroundResource(R.drawable.daylight)
        }
        else -> {
            imageFilterCover.invisible()
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
        else -> {
            imageFilterCover.invisible()
        }
    }
}
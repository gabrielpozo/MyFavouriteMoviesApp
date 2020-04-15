package com.light.finder.extensions

import android.content.Context
import android.view.View
import com.light.finder.R
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

fun Context.getColorName(colorCode: Int): String = when (colorCode) {
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

    else -> colorCode.toString()
}


fun Context.getFinishName(finishCode: Int): String = when (finishCode) {
    1 -> getString(R.string.clear)
    2 -> getString(R.string.frosted)
    else -> finishCode.toString()
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
            invisible()
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
            invisible()
        }
    }
}
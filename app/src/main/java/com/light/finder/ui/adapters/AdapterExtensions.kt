package com.light.finder.ui.adapters

import android.view.View
import com.light.domain.model.FilterVariationCF
import com.light.finder.R
import kotlinx.android.synthetic.main.item_button_filter_unselected.view.*
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

fun View.setDrawableBackgroundWattage(filter: FilterVariationCF) {
    if (!filter.isAvailable) {
        wattageButton.setBackgroundResource(R.drawable.button_filter_disabled)
        wattageButton.setTextAppearance(R.style.ButtonDisabled)
    } else {
        wattageButton.setBackgroundResource(R.drawable.button_filter_unselected)
        wattageButton.setTextAppearance(R.style.ButtonUnSelected)
    }

    if (filter.isSelected) {
        wattageButton.setBackgroundResource(R.drawable.button_filter_selected)
        wattageButton.setTextAppearance(R.style.ButtonSelected)
    }

}

fun List<FilterVariationCF>.setBackgroundLayout(viewItemsMap: HashMap<Int, View>) {
    forEach { filter ->
        val itemView = viewItemsMap[filter.codeFilter]
        itemView?.setDrawableOnBackground(filter)
    }
}

fun View.setDrawableOnBackground(filter: FilterVariationCF) {
    if (!filter.isAvailable) {
        setBackgroundResource(R.drawable.card_filter_disabled)
        variation_name.setTextAppearance(R.style.DisabledText)
        availableOptions.visibility = View.VISIBLE
        gradientView.visibility = View.VISIBLE

    } else {
        setBackgroundResource(R.drawable.card_filter_unselected)
        variation_name.setTextAppearance(R.style.InactiveTextDark)
        availableOptions.visibility = View.GONE
        gradientView.visibility = View.GONE
    }

    if (filter.isSelected) {
        setBackgroundResource(R.drawable.card_filter_selected)
        variation_name.setTextAppearance(R.style.ActiveText)
        availableOptions.visibility = View.GONE
        gradientView.visibility = View.GONE

    }

}

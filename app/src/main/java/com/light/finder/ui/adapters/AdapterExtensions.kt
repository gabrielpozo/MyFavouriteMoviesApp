package com.light.finder.ui.adapters

import android.view.View
import com.light.domain.model.FilterVariation
import com.light.finder.R
import kotlinx.android.synthetic.main.item_button_filter_unselected.view.*
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

fun View.setDrawableBackgroundWattage(filter: FilterVariation) {
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

fun List<FilterVariation>.setBackgroundLayout(viewItemsMap: HashMap<String, View>) {
    forEach { filter ->
        val itemView = viewItemsMap[filter.nameFilter]
        itemView?.setDrawableOnBackground(filter)
    }
}

fun View.setDrawableOnBackground(filter: FilterVariation) {
    if (!filter.isAvailable) {
        setBackgroundResource(R.drawable.card_filter_disabled)
        variation_name.setTextAppearance(R.style.DisabledText)

    } else {
        setBackgroundResource(R.drawable.card_filter_unselected)
        variation_name.setTextAppearance(R.style.InactiveTextDark)
    }

    if (filter.isSelected) {
        setBackgroundResource(R.drawable.card_filter_selected)
        variation_name.setTextAppearance(R.style.ActiveText)
    }

}
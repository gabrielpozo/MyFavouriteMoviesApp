package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FilterVariation
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_button_filter_unselected.view.*
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*


class FilterWattageAdapter(private val listener: (FilterVariation) -> Unit) :
    RecyclerView.Adapter<FilterWattageAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<String, View>()


    var filterListWattage: List<FilterVariation> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new ->
            old.nameFilter == new.nameFilter
        }, shouldRefreshData = false
    )


    fun updateBackgroundAppearance(filterWattageList: List<FilterVariation>) {
        filterWattageList.forEach { filter ->
            val itemView = viewItemsMap[filter.nameFilter]
            if (!filter.isAvailable) {
                itemView?.wattageButton?.setBackgroundResource(R.drawable.button_filter_disabled)
                itemView?.wattageButton?.setTextAppearance(R.style.ButtonDisabled)

            } else {
                itemView?.wattageButton?.setBackgroundResource(R.drawable.button_filter_unselected)
                itemView?.wattageButton?.setTextAppearance(R.style.ButtonUnSelected)
            }

            if (filter.isSelected) {
                itemView?.wattageButton?.setBackgroundResource(R.drawable.button_filter_selected)
                itemView?.wattageButton?.setTextAppearance(R.style.ButtonSelected)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_button_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListWattage.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListWattage[position]
        if (!viewItemsMap.containsKey(filter.nameFilter)) {
            viewItemsMap[filter.nameFilter] = holder.itemView
        }

        holder.bind(filter)
        holder.itemView.wattageButton.setOnClickListener {
            listener(filterListWattage[position])
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariation) {
            itemView.wattageButton.text = String.format(
                itemView.context.getString(R.string.wattage_variation),
                filter.nameFilter
            )
            if (!filter.isAvailable) {
                itemView.wattageButton.setBackgroundResource(R.drawable.button_filter_disabled)
                itemView.wattageButton.setTextAppearance(R.style.ButtonDisabled)
            } else {
                itemView.wattageButton.setBackgroundResource(R.drawable.button_filter_unselected)
                itemView.wattageButton.setTextAppearance(R.style.ButtonUnSelected)
            }

            if (filter.isSelected) {
                itemView.wattageButton.setBackgroundResource(R.drawable.button_filter_selected)
                itemView.wattageButton.setTextAppearance(R.style.ButtonSelected)
            }
        }

    }
}


class FilterColorAdapter(private val listener: (FilterVariation) -> Unit) :
    RecyclerView.Adapter<FilterColorAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<String, View>()

    var filterListColor: List<FilterVariation> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter },
        shouldRefreshData = false
    )

    fun updateBackgroundAppearance(FilterVariationList: List<FilterVariation>) {
        FilterVariationList.forEach { filter ->
            val itemView = viewItemsMap[filter.nameFilter]
            if (!filter.isAvailable) {
                itemView?.setBackgroundResource(R.drawable.card_filter_disabled)
                itemView?.variation_name?.setTextAppearance(R.style.DisabledText)

            } else {
                itemView?.setBackgroundResource(R.drawable.card_filter_unselected)
                itemView?.variation_name?.setTextAppearance(R.style.InactiveTextDark)
            }

            if (filter.isSelected) {
                itemView?.setBackgroundResource(R.drawable.card_filter_selected)
                itemView?.variation_name?.setTextAppearance(R.style.ActiveText)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListColor.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListColor[position]
        if (!viewItemsMap.containsKey(filter.nameFilter)) {
            viewItemsMap[filter.nameFilter] = holder.itemView
        }
        holder.bind(filter)

        holder.itemView.setOnClickListener {
            listener(filterListColor[position])
        }
        // holder.itemView.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariation) {
            itemView.variation_name.text = filter.nameFilter
            if (!filter.isAvailable) {
                itemView.setBackgroundResource(R.drawable.card_filter_disabled)
                itemView.variation_name.setTextAppearance(R.style.DisabledText)

            } else {
                itemView.setBackgroundResource(R.drawable.card_filter_unselected)
                itemView.variation_name.setTextAppearance(R.style.InactiveTextDark)

            }

            if (filter.isSelected) {
                itemView.setBackgroundResource(R.drawable.card_filter_selected)
                itemView.variation_name.setTextAppearance(R.style.ActiveText)
            }
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}


class FilterFinishAdapter(private val listener: (FilterVariation) -> Unit) :
    RecyclerView.Adapter<FilterFinishAdapter.ViewHolder>() {
    private val viewItemsMap = hashMapOf<String, View>()

    var filterListFinish: List<FilterVariation> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter },
        shouldRefreshData = false
    )


    fun updateBackgroundAppearance(FilterVariationList: List<FilterVariation>) {
        FilterVariationList.forEach { filter ->
            val itemView = viewItemsMap[filter.nameFilter]
            if (!filter.isAvailable) {
                itemView?.setBackgroundResource(R.drawable.card_filter_disabled)
                itemView?.variation_name?.setTextAppearance(R.style.DisabledText)

            } else {
                itemView?.setBackgroundResource(R.drawable.card_filter_unselected)
                itemView?.variation_name?.setTextAppearance(R.style.InactiveTextDark)
            }

            if (filter.isSelected) {
                itemView?.setBackgroundResource(R.drawable.card_filter_selected)
                itemView?.variation_name?.setTextAppearance(R.style.ActiveText)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListFinish.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListFinish[position]
        if (!viewItemsMap.containsKey(filter.nameFilter)) {
            viewItemsMap[filter.nameFilter] = holder.itemView
        }
        holder.bind(filter)
        holder.itemView.setOnClickListener {
            listener(filterListFinish[position])
        }
        // holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariation) {
            itemView.variation_name.text = filter.nameFilter
            if (!filter.isAvailable) {
                itemView.setBackgroundResource(R.drawable.card_filter_disabled)
                itemView.variation_name.setTextAppearance(R.style.DisabledText)

            } else {
                itemView.setBackgroundResource(R.drawable.card_filter_unselected)
                itemView.variation_name.setTextAppearance(R.style.InactiveTextDark)

            }

            if (filter.isSelected) {
                itemView.setBackgroundResource(R.drawable.card_filter_selected)
                itemView.variation_name.setTextAppearance(R.style.ActiveText)

            }
            //itemView.imageFilterCover.loadUrl(filter.imageFinishCover)
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}
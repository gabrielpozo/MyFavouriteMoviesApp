package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FilterVariationCF
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_button_filter_unselected.view.*
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*


class FilterWattageAdapter(private val listener: (FilterVariationCF) -> Unit) :
    RecyclerView.Adapter<FilterWattageAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<Int, View>()


    var filterListWattage: List<FilterVariationCF> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new ->
            old.codeFilter == new.codeFilter
        }, shouldRefreshData = false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_button_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListWattage.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListWattage[position]
        if (!viewItemsMap.containsKey(filter.codeFilter)) {
            viewItemsMap[filter.codeFilter] = holder.itemView
        }

        holder.bind(filter)
        holder.itemView.wattageButton.setOnClickListener {
            listener(filterListWattage[position])
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF) {
            itemView.wattageButton.text = String.format(
                itemView.context.getString(R.string.wattage_variation),
                filter.codeFilter.toString()
            )
            itemView.setDrawableBackgroundWattage(filter)
        }

    }

    fun updateBackgroundAppearance(filterWattageList: List<FilterVariationCF>) {
        filterWattageList.forEach { filter ->
            val itemView = viewItemsMap[filter.codeFilter]
            itemView?.setDrawableBackgroundWattage(filter)
        }

    }
}


class FilterColorAdapter(private val listener: (FilterVariationCF) -> Unit) :
    RecyclerView.Adapter<FilterColorAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<Int, View>()

    var filterListColor: List<FilterVariationCF> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.codeFilter == new.codeFilter },
        shouldRefreshData = false
    )

    fun updateBackgroundAppearance(filterVariationList: List<FilterVariationCF>) {
        filterVariationList.setBackgroundLayout(viewItemsMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListColor.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListColor[position]
        if (!viewItemsMap.containsKey(filter.codeFilter)) {
            viewItemsMap[filter.codeFilter] = holder.itemView
        }
        holder.bind(filter)

        holder.itemView.setOnClickListener {
            listener(filterListColor[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF) {
            itemView.variation_name.text = itemView.context.getColorName(filter.codeFilter)
            itemView.setDrawableOnBackground(filter)
            itemView.setColorVariation(filter.codeFilter)
        }
    }
}


class FilterFinishAdapter(private val listener: (FilterVariationCF) -> Unit) :
    RecyclerView.Adapter<FilterFinishAdapter.ViewHolder>() {
    private val viewItemsMap = hashMapOf<Int, View>()

    var filterListFinish: List<FilterVariationCF> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.codeFilter == new.codeFilter },
        shouldRefreshData = false
    )


    fun updateBackgroundAppearance(filterVariationList: List<FilterVariationCF>) {
        filterVariationList.setBackgroundLayout(viewItemsMap)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListFinish.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListFinish[position]
        if (!viewItemsMap.containsKey(filter.codeFilter)) {
            viewItemsMap[filter.codeFilter] = holder.itemView
        }
        holder.bind(filter)
        holder.itemView.setOnClickListener {
            listener(filterListFinish[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF) {
            itemView.variation_name.text = itemView.context.getFinishName(filter.codeFilter)
            itemView.setDrawableOnBackground(filter)
            itemView.setFinishVariation(filter.codeFilter)
        }
    }
}

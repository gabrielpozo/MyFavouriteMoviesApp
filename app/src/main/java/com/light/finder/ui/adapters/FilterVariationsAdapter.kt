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
            itemView.setDrawableBackgroundWattage(filter)
        }

    }

    fun updateBackgroundAppearance(filterWattageList: List<FilterVariation>) {
        filterWattageList.forEach { filter ->
            val itemView = viewItemsMap[filter.nameFilter]
            itemView?.setDrawableBackgroundWattage(filter)
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

    fun updateBackgroundAppearance(filterVariationList: List<FilterVariation>) {
        filterVariationList.setBackgroundLayout(viewItemsMap)
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
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariation) {
            itemView.variation_name.text = filter.nameFilter
            itemView.setDrawableOnBackground(filter)
            when (filter.nameFilter) {
                itemView.context.getString(R.string.warm) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.warm)
                }
                itemView.context.getString(R.string.white_warm) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.warm_white)
                }

                itemView.context.getString(R.string.cool_white) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.cool_white)
                }

                itemView.context.getString(R.string.daylight) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.daylight)
                }
            }
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


    fun updateBackgroundAppearance(filterVariationList: List<FilterVariation>) {
        filterVariationList.setBackgroundLayout(viewItemsMap)
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
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariation) {
            itemView.variation_name.text = filter.nameFilter
            itemView.setDrawableOnBackground(filter)
            when (filter.nameFilter) {
                itemView.context.getString(R.string.frosted) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.frosted)
                }
                itemView.context.getString(R.string.clear) -> {
                    itemView.imageFilterCover.setBackgroundResource(R.drawable.clear)
                }


            }
        }
    }
}

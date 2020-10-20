package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.CctType
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.FinishType
import com.light.domain.model.ProductConnectivity
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
        holder.itemView.wattageButton.setSafeOnClickListener {
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


class FilterColorAdapter(
    private val listener: (FilterVariationCF) -> Unit,
    private val filterColorList: List<CctType> = emptyList()
) :
    RecyclerView.Adapter<FilterColorAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<Int, View>()

    var filterListColor: List<FilterVariationCF> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.codeFilter == new.codeFilter },
        shouldRefreshData = false
    )

    fun setColorFromAmbiance(color: Int) {
        filterListColor.forEachIndexed { index, element ->
            if (element.codeFilter == color) {
                listener(filterListColor[index])
            }
        }
    }

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
        holder.bind(filter, filterColorList)

        holder.itemView.setSafeOnClickListener {
            listener(filterListColor[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF, filterColorList: List<CctType>) {
            itemView.variation_name.text = getLegendCctTagPref(
                filter.codeFilter,
                filterTypeList = filterColorList,
                legendTag = COLOR_LEGEND_TAG
            )
            itemView.setDrawableOnBackground(filter)
            itemView.imageFilterCover.loadThumbnailVariation(
                getLegendCctTagPrefIcon(
                    filter.codeFilter,
                    filterTypeList = filterColorList,
                    legendTag = COLOR_LEGEND_TAG
                )
            )
        }
    }
}


class FilterFinishAdapter(
    private val listener: (FilterVariationCF) -> Unit,
    private val filterFinishList: List<FinishType> = emptyList()
) :
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
        holder.bind(filter, filterFinishList)
        holder.itemView.setSafeOnClickListener {
            listener(filterListFinish[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF, filterFinishList: List<FinishType>) {
            itemView.variation_name.text = getLegendFinishTagPref(
                filter.codeFilter,
                filterTypeList = filterFinishList,
                legendTag = FINISH_LEGEND_TAG
            )
            itemView.setDrawableOnBackground(filter)

            itemView.imageFilterCover.loadThumbnailVariation(
                getLegendFinishTagPrefImage(
                    filter.codeFilter,
                    filterTypeList = filterFinishList,
                    legendTag = FINISH_LEGEND_TAG
                )
            )
        }
    }
}

class FilterConnectivityAdapter(
    private val listener: (FilterVariationCF) -> Unit,
    private val filterConnectivityList: List<ProductConnectivity> = emptyList()
) :
    RecyclerView.Adapter<FilterConnectivityAdapter.ViewHolder>() {
    private val viewItemsMap = hashMapOf<Int, View>()

    var filterListConnectivity: List<FilterVariationCF> by basicDiffUtil(
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

    override fun getItemCount(): Int = filterListConnectivity.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListConnectivity[position]
        if (!viewItemsMap.containsKey(filter.codeFilter)) {
            viewItemsMap[filter.codeFilter] = holder.itemView
        }
        holder.bind(filter, filterConnectivityList)
        holder.itemView.setSafeOnClickListener {
            listener(filterListConnectivity[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterVariationCF, filterConnectivityList: List<ProductConnectivity>) {
            itemView.variation_name.text = getLegendConnectivityTagPref(
                filter.codeFilter,
                filterTypeList = filterConnectivityList,
                legendTag = CONNECTIVITY_LEGEND_TAG
            )
            itemView.setDrawableOnBackground(filter)

            itemView.imageFilterCover.loadThumbnailVariation(
                getLegendConnectivityTagPrefImage(
                    filter.codeFilter,
                    filterTypeList = filterConnectivityList,
                    legendTag = CONNECTIVITY_LEGEND_TAG
                )
            )
        }
    }
}

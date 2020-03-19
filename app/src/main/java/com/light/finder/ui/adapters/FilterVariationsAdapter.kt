package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FilterColor
import com.light.domain.model.FilterFinish
import com.light.domain.model.FilterWattage
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_button_filter_unselected.view.*
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*


class FilterWattageAdapter(private val listener: (FilterWattage) -> Unit) :
    RecyclerView.Adapter<FilterWattageAdapter.ViewHolder>() {

    var filterList: List<FilterWattage> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_button_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        holder.itemView.setOnClickListener { listener(filter) }
        //holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterWattage) {
            itemView.wattageButton.text = filter.nameFilter
            //itemView.filterButton.setOnClickListener { listener(filter) }
            if (filter.isSelected) {
                itemView.wattageButton.setBackgroundResource(R.drawable.button_filter_selected)
            } else {
                itemView.wattageButton.setBackgroundResource(R.drawable.button_filter_unselected)
            }
        }
    }
}


class FilterColorAdapter(private val listener: (FilterColor) -> Unit) :
    RecyclerView.Adapter<FilterColorAdapter.ViewHolder>() {

    var filterList: List<FilterColor> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        holder.itemView.setOnClickListener { listener(filter) }
        //holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterColor) {
            itemView.variation_name.text = filter.nameFilter
            if (filter.isSelected) {
                itemView.setBackgroundResource(R.drawable.card_filter_selected)
            } else {
                itemView.setBackgroundResource(R.drawable.button_filter_unselected)
            }
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}


class FilterFinishAdapter(private val listener: (FilterFinish) -> Unit) :
    RecyclerView.Adapter<FilterFinishAdapter.ViewHolder>() {

    var filterList: List<FilterFinish> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        holder.itemView.setOnClickListener { listener(filter) }
        // holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterFinish) {
            itemView.variation_name.text = filter.nameFilter
            if (filter.isSelected) {
                itemView.setBackgroundResource(R.drawable.card_filter_selected)
            } else {
                itemView.setBackgroundResource(R.drawable.button_filter_unselected)
            }
            //itemView.imageFilterCover.loadUrl(filter.imageFinishCover)
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}
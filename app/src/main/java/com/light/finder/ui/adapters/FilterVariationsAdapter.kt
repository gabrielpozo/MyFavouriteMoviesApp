package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FilterWattage
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_filter.view.*


class FilterWattageAdapter(private val listener: (FilterWattage) -> Unit) :
    RecyclerView.Adapter<FilterWattageAdapter.ViewHolder>() {

    var filterList: List<FilterWattage> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_filter, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        //holder.itemView.setOnClickListener { listener(filter) }
        holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterWattage) {
            itemView.filterButton.text = filter.nameFilter
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}


class FilterColorAdapter(private val listener: (FilterWattage) -> Unit) :
    RecyclerView.Adapter<FilterColorAdapter.ViewHolder>() {

    var filterList: List<FilterWattage> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_filter, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        //holder.itemView.setOnClickListener { listener(filter) }
        holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterWattage) {
            itemView.filterButton.text = filter.nameFilter
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}



class FilterFinishAdapter(private val listener: (FilterWattage) -> Unit) :
    RecyclerView.Adapter<FilterFinishAdapter.ViewHolder>() {

    var filterList: List<FilterWattage> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_filter, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterList[position]
        holder.bind(filter)
        //holder.itemView.setOnClickListener { listener(filter) }
        holder.itemView.filterButton.setOnClickListener { listener(filter) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: FilterWattage) {
            itemView.filterButton.text = filter.nameFilter
            //itemView.filterButton.setOnClickListener { listener(filter) }
        }
    }
}
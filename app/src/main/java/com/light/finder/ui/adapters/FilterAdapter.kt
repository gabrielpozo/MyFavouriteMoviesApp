package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import com.light.domain.model.Filter
import kotlinx.android.synthetic.main.item_filter.view.*

//todo must be horizontal gridLayout for 2 grids
class FilterAdapter(private val listener: (Filter) -> Unit) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

        var filterList: List<Filter> by basicDiffUtil(
            emptyList(),
            areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter}
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
            fun bind(filter: Filter) {
                itemView.filterButton.text = filter.nameFilter
                //itemView.filterButton.setOnClickListener { listener(filter) }
            }
        }
}
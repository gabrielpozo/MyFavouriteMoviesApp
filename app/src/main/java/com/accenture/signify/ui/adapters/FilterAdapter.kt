package com.accenture.signify.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.accenture.signify.R
import com.accenture.signify.extensions.basicDiffUtil
import com.accenture.signify.extensions.inflate
import com.accenture.domain.model.Filter
import kotlinx.android.synthetic.main.item_filter.view.*

//todo must be horizontal gridLayout for 2 grids
class FilterAdapter(private val listener: (Filter) -> Unit) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

        var categories: List<Filter> by basicDiffUtil(
            emptyList(),
            areItemsTheSame = { old, new -> old.nameFilter == new.nameFilter}
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = parent.inflate(R.layout.item_filter, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = categories.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val filter = categories[position]
            holder.bind(filter)
            holder.itemView.setOnClickListener { listener(filter) }
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(filter: Filter) {
                itemView.textViewFilter.text = filter.nameFilter
            }
        }
}
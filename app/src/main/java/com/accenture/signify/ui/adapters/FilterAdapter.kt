package com.accenture.signify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.accenture.signify.R
import kotlinx.android.synthetic.main.item_filter.view.*

//todo must be horizontal gridLayout for 2 grids
class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    var list: List<Int> = arrayListOf()
    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val number = list[position]
        tracker?.let {
            holder.bind(number, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long = position.toLong()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(value: Int, selected: Boolean = false) {
            itemView.textViewFilter.text = value.toString()
            itemView.isActivated = selected
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }
}
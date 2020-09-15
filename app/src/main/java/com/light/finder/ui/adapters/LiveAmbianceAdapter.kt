package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.CctType
import com.light.finder.R
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadCircleImage
import com.light.finder.extensions.setSafeOnClickListener
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

class LiveAmbianceAdapter(
    private val listener: (CctType) -> Unit,
    private val filterListColor: List<CctType> = emptyList()
) :
    RecyclerView.Adapter<LiveAmbianceAdapter.ViewHolder>() {
    private var lastPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.filter_background, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListColor.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListColor[position]

        holder.bind(filter)
        holder.itemView.setSafeOnClickListener {
            listener(filter)
            filterListColor[lastPosition].isSelected = false
            filterListColor[position].isSelected = true
            lastPosition = position
            notifyDataSetChanged()
        }

        if (filter.isSelected) {
            lastPosition = position
            holder.itemView.frame.setBackgroundResource(R.drawable.circle_background_green)
        } else {
            holder.itemView.frame.setBackgroundResource(R.drawable.circle_background_grey)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: CctType) {
            itemView.variation_name.text = filter.name
            itemView.imageFilterCover.loadCircleImage(filter.bigIcon)
        }
    }
}


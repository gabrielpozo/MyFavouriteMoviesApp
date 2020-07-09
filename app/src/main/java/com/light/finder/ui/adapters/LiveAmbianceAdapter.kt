package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.CctType
import com.light.finder.R
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadCircleImage
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*

class LiveAmbianceAdapter(
    private val listener: (CctType) -> Unit,
    private val filterListColor: List<CctType> = emptyList()
) :
    RecyclerView.Adapter<LiveAmbianceAdapter.ViewHolder>() {

    private val viewItemsMap = hashMapOf<Int, View>()


   /* fun updateBackgroundAppearance(filterVariationList: List<FilterVariationCF>) {
        filterVariationList.setBackgroundLayout(viewItemsMap)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_card_filter_unselected, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterListColor.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filterListColor[position]

        holder.bind(filter)
        holder.itemView.setOnClickListener {
            listener(filter)
        }
    }

    //todo change background according to click
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(filter: CctType) {
            itemView.variation_name.text = filter.name
           // itemView.setBackgroundResource(R.drawable.circle_background_green)
            itemView.imageFilterCover.loadCircleImage(filter.bigIcon)
        }
    }
}


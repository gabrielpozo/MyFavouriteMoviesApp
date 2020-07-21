package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FittingBrowsing
import com.light.finder.R
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_browse_fitting.view.*

class BrowseFittingAdapter(
    private val listener: (FittingBrowsing) -> Unit,
    private val productsList: List<FittingBrowsing> = emptyList()
) :
    RecyclerView.Adapter<BrowseFittingAdapter.ViewHolder>() {
    private var lastPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_browse_fitting, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productsList[position]

        holder.bind(product)
        holder.itemView.setOnClickListener {
            listener(product)
            productsList[lastPosition].isSelected = false
            productsList[position].isSelected = true
            lastPosition = position
            notifyDataSetChanged()
        }

        if (product.isSelected) {
            lastPosition = position
            holder.itemView.fittingBg.setBackgroundResource(R.drawable.browse_rounded_edge)
        } else {
            holder.itemView.fittingBg.setBackgroundResource(R.drawable.browse_rounded_edge_unselected)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: FittingBrowsing) {
            itemView.textBrowseResults.text = product.name
            //itemView.imageViewFinishIcon.loadUrl()
            //todo wtf am i supposed to display here?
        }
    }
}


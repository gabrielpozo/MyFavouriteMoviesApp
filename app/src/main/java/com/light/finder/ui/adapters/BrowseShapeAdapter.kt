package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.ProductBrowsing
import com.light.finder.R
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_browse_fitting.view.*

class BrowseShapeAdapter(
    private val listener: (ProductBrowsing) -> Unit,
    private val productsList: List<ProductBrowsing> = emptyList()
) :
    RecyclerView.Adapter<BrowseShapeAdapter.ViewHolder>() {
    private var lastPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_browse_shape, false)
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
        fun bind(product: ProductBrowsing) {
            //itemView.textBrowseResults.text =
            //itemView.imageViewFinishIcon.loadUrl()
        }
    }
}


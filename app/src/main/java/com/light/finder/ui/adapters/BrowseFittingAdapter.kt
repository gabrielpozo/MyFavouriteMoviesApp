package com.light.finder.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.FormFactorTypeBaseId
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadFitting
import kotlinx.android.synthetic.main.item_browse_fitting.view.*

class BrowseFittingAdapter(
    private val listener: (FormFactorTypeBaseId) -> Unit
) : RecyclerView.Adapter<BrowseFittingAdapter.ViewHolder>() {
    var productsList: List<FormFactorTypeBaseId> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    fun setFittingProductList(formFactorBaseIdList: List<FormFactorTypeBaseId>){
        productsList = formFactorBaseIdList
    }

    private var lastPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_browse_fitting, false)
        return ViewHolder(view)
    }

    fun clearSelection() {
        productsList.forEach {
            it.isSelected = false
        }
        lastPosition = 0
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productsList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

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
        fun bind(product: FormFactorTypeBaseId) {
            itemView.textBrowseResults.text = product.name
            when (product.image) {
                null -> {
                    itemView.imageViewFinishIcon.setBackgroundResource(R.color.backgroundLight)
                }
                else -> {
                    itemView.imageViewFinishIcon.loadFitting(product.image!!)
                }
            }
        }
    }
}


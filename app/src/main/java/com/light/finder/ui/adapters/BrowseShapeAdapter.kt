package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.ShapeBrowsing
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadFitting
import kotlinx.android.synthetic.main.item_browse_shape.view.*

class BrowseShapeAdapter(
    private val listener: (ShapeBrowsing) -> Unit
) : RecyclerView.Adapter<BrowseShapeAdapter.ViewHolder>() {
    var productsList: List<ShapeBrowsing> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    fun setShapeProductList(formFactorBaseIdList: List<ShapeBrowsing>) {
        productsList = formFactorBaseIdList.map { it.copy() }
    }

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
            //callback(true)
            listener(product)
            productsList[lastPosition].isSelected = false
            productsList[position].isSelected = true
            lastPosition = position
            notifyDataSetChanged()
        }


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(product: ShapeBrowsing) {
            itemView.textBrowseBulbName.text = product.name
            when (product.image) {
                null -> {
                    itemView.imageViewBrowseIcon.setBackgroundResource(R.color.backgroundLight)
                }
                else -> {
                    itemView.imageViewBrowseIcon.loadFitting(product.image!!)
                }
            }

            when {
                product.subtitleCount > 1 -> {
                    itemView.textBrowseResultsCount.text = String.format(
                        itemView.context.getString(R.string.text_results),
                        product.subtitleCount
                    )
                }
                product.subtitleCount == 1 -> {
                    itemView.textBrowseResultsCount.text = String.format(
                        itemView.context.getString(R.string.text_result),
                        product.subtitleCount
                    )
                }
                else -> {
                    itemView.textBrowseResultsCount.text =
                        itemView.context.getString(R.string.not_available)
                }
            }


        }
    }
}


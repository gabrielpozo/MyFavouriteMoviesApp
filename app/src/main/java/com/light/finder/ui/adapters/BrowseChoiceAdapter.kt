package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.ChoiceBrowsing
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadFitting
import com.light.finder.extensions.setSafeOnClickListener
import kotlinx.android.synthetic.main.item_browse_choice.view.*

class BrowseChoiceAdapter(
    private val listener: (ChoiceBrowsing) -> Unit
) : RecyclerView.Adapter<BrowseChoiceAdapter.ViewHolder>() {
    private var productsList: List<ChoiceBrowsing> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    fun setChoiceProductList(formFactorBaseIdList: List<ChoiceBrowsing>) {
        productsList = formFactorBaseIdList.map { it.copy() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_browse_choice, false)
        return ViewHolder(view)
    }

    fun clearSelection() {
        productsList.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productsList[position]
        holder.bind(product)
        holder.itemView.setSafeOnClickListener {
            productsList[position].isSelected = !productsList[position].isSelected
            listener(product)
            notifyDataSetChanged()
        }

        if (product.isSelected) {
            holder.itemView.choiceBg.setBackgroundResource(R.drawable.browse_rounded_edge)
        } else {
            holder.itemView.choiceBg.setBackgroundResource(R.drawable.browse_rounded_edge_unselected)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(product: ChoiceBrowsing) {
            itemView.textBrowseBulbName.text = product.name
            itemView.textBrowseBulbDesc.text = product.description
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
                    makeItemDisabled()
                }
            }
        }

        private fun makeItemDisabled() {
            itemView.isClickable = false
            itemView.isEnabled = false
            itemView.textBrowseBulbName.alpha = 0.4F
            itemView.imageViewBrowseIcon.alpha = 0.4F
            itemView.textBrowseBulbDesc.alpha = 0.4F
            itemView.textBrowseResultsCount.text =
                itemView.context.getString(R.string.not_available_choice)
        }
    }
}


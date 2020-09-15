package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.category_results_header.view.*
import kotlinx.android.synthetic.main.item_category.view.priceButton
import kotlinx.android.synthetic.main.item_results.view.*


class CategoriesAdapter(
    private val listener: (Category) -> Unit,
    private val message: Message
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val headerOffset: Int = 1

    var categories: List<Category> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.categoryIndex == new.categoryIndex }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_HEADER -> {
                // Here Inflating the header view
                val view = parent.inflate(R.layout.category_results_header, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                val view = parent.inflate(R.layout.item_results, false)
                ItemViewHolder(view)
            }
            else -> {
                throw ClassCastException("Unknown viewType $viewType")
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            message: Message
        ) {

            itemView.text_identified.text = message.textIdentified
            itemView.identifiedBulb.loadIdentified(message.imageIdentified)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    // Size + headerOffset since 0 is reserved for header
    override fun getItemCount(): Int = categories.size + headerOffset

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(message)
        } else if (holder is ItemViewHolder) {
            val category = categories[position - headerOffset]
            holder.bind(
                category
            )

            holder.itemView.setSafeOnClickListener { listener(category) }
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")

        fun bind(
            category: Category
        ) {
            itemView.category_title.text = category.categoryName
            itemView.priceButton.text =
                itemView.context.getString(R.string.category_price, category.priceRange)
            itemView.category_description.text = category.categoryDescription
            itemView.category_image.loadUrWithoutPlaceholderl(category.categoryImage)
        }
    }
}
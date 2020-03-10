package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.Category
import com.light.finder.R
import com.light.finder.extensions.basicDiffUtil
import com.light.finder.extensions.getStringFormatter
import com.light.finder.extensions.inflate
import com.light.finder.extensions.loadUrl
import kotlinx.android.synthetic.main.item_category.view.*


class CategoriesAdapter(private val listener: (Category) -> Unit) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var categories: List<Category> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.categoryIndex == new.categoryIndex }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_category, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
        holder.itemView.setOnClickListener { listener(category) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(category: Category) {
            itemView.category_name.text = category.categoryName
            itemView.price.text = category.priceRange
            itemView.bulbCover.loadUrl(category.categoryImage)
            if (category.minWattage != itemView.context.getString(R.string.no_value)) {
                itemView.product_detail.text =
                    itemView.context.getString(
                        R.string.description_wattage,
                        category.minWattage,
                        category.maxWattage,
                        category.categoryProductBase
                    )
            }
        }
    }
}
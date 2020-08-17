package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.Category
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.priceButton
import kotlinx.android.synthetic.main.item_results.view.*


class CategoriesAdapter(
    private val listener: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var categories: List<Category> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.categoryIndex == new.categoryIndex }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_results, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        holder.bind(
            category
        )
        holder.itemView.setSafeOnClickListener { listener(category) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")

        fun bind(
            category: Category
        ) {
            itemView.category_title.text = category.categoryName
            itemView.priceButton.text = itemView.context.getString(R.string.category_price, category.priceRange)
            itemView.category_description.text = category.categoryDescription
            itemView.category_image.loadUrWithoutPlaceholderl(category.categoryImage)
        }
    }
}
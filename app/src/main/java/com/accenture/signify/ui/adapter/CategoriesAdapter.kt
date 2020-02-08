package com.accenture.signify.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.accenture.domain.model.Category
import com.accenture.signify.R
import com.accenture.signify.extensions.basicDiffUtil
import com.accenture.signify.extensions.inflate
import com.accenture.signify.extensions.loadUrl
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
        fun bind(category: Category) {
            itemView.category_name.text = category.categoryName
            itemView.price.text = category.categoryPrice
            itemView.bulbCover.loadUrl(category.categoryImage)
        }
    }
}
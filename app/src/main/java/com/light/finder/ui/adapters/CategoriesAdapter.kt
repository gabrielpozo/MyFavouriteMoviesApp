package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.Category
import com.light.finder.R
import com.light.finder.extensions.*
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
        holder.bind(category, position)
        holder.itemView.setOnClickListener { listener(category) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(category: Category, position: Int) {
            var colorText = ""
            itemView.category_name.text = category.categoryName
            itemView.price.text = category.priceRange
            itemView.bulbCover.loadUrl(category.categoryImage)
            //TODO it will change once we have the field on the api(most efficient option)
            /*if (position == 0) {
                itemView.energyButton.visibility = View.VISIBLE
            }*/
            //
            category.colors.forEachIndexed { index, color ->
                colorText = if (index != category.colors.size - 1) {
                    "$colorText$color <br>"
                } else {
                    "$colorText$color"
                }

            }


            itemView.product_color.text = Html.fromHtml(colorText).toString()
            //itemView.product_color.endDrawable()

            val minMaxWattage = itemView.context.getString(
                R.string.description_wattage,
                category.minWattage,
                category.maxWattage,
                category.categoryProductBase
            )

            
            if (category.minWattage != itemView.context.getString(R.string.no_value)) {
                itemView.product_detail.text = minMaxWattage.replace("-W", "")

            }
        }
    }
}
package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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

        val maxEnergySaving = categories.maxBy { it.maxEnergySaving }?.maxEnergySaving

        holder.bind(category, maxEnergySaving ?: -0.0f)
        holder.itemView.setOnClickListener { listener(category) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(category: Category, maxSaving: Float) {
            itemView.category_name.text = category.categoryName
            itemView.price.text = category.priceRange
            itemView.bulbCover.loadUrl(category.categoryImage)
            category.maxEnergySaving
            if (maxSaving == category.maxEnergySaving) {
                itemView.energyButton.visibility = View.VISIBLE
            }


            category.colors.forEachIndexed { index, color ->
                val textView = TextView(itemView.context)
                textView.text = color
                textView.endDrawableIcon(textView.getColorString(color))
                textView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.setTextAppearance(R.style.SubTitleField)
                if (index < category.colors.size - 1) {
                    textView.setPadding(0, 0, 0, 32)
                } else if( category.colors.size == 1){
                    textView.setPadding(0, 0, 0, 8)
                }
                //textView.compoundDrawablePadding = 32
                itemView.textViewsLayout.addView(textView)
            }

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
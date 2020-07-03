package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.Category
import com.light.domain.model.FilterType
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(
    private val listener: (Category) -> Unit,
    private val filterColorList: List<FilterType> = emptyList()
) :
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

        val indexes = getMaxIndices(categories)

        holder.itemView.colorsLayout.removeAllViews()
        holder.itemView.wattageLayout.removeAllViews()
        holder.itemView.imagesLayout.removeAllViews()
        holder.bind(category, indexes, categories.size, position, filterColorList)
        holder.itemView.setOnClickListener { listener(category) }
    }

    private fun getMaxIndices(categories: List<Category>): List<Int> {
        val max = categories.maxBy { it.maxEnergySaving }?.maxEnergySaving

        val maxIndices = mutableListOf<Int>()
        for (i in categories.indices) {
            if (categories[i].maxEnergySaving == max) {
                maxIndices.add(i)
            }
        }
        return maxIndices
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")

        fun bind(
            category: Category,
            indexes: List<Int>,
            categoriesSize: Int,
            position: Int,
            filterColorList: List<FilterType>
        ) {
            itemView.category_name.text = category.categoryName
            itemView.priceButton.text = category.priceRange
            itemView.bulbCover.loadUrl(category.categoryImage)
            itemView.thumbnail.loadThumbnail(category.categoryImage)
            itemView.bulbName.text = itemView.context.getString(R.string.bulb_s).format(category.categoryProductBase)

            if (indexes.size == 1 && position == indexes[0] && categoriesSize > 1) {
                itemView.energyButton.text = "Most energy efficient"
                itemView.energyButton.visible()
            } else if (indexes.size in 2 until categoriesSize) {
                for (i in indexes) {
                    if (i == position) {
                        itemView.energyButton.text = "More energy efficient"
                        itemView.energyButton.visible()
                    }
                }
            } else if (indexes.size == categoriesSize) {
                itemView.energyButton.gone()
            } else {
                itemView.energyButton.gone()
            }

            category.colors.forEachIndexed { index, colorCode ->
                val imageView = ImageView(itemView.context)
                /*textView.text = getLegendTagPref(
                    colorCode,
                    filterTypeList = filterColorList,
                    legendTag = "product_cct_code"
                )*/
                val drawable = itemView.context.getColorDrawable(colorCode)
                if (drawable != 0) {
                    imageView.setBackgroundResource(drawable)
                }
                imageView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                //textView.setTextAppearance(R.style.SubTitleField)
                if (index < category.colors.size - 1) {
                    imageView.setPadding(0, 0, 0, 32)
                } else if (category.colors.size == 1) {
                    imageView.setPadding(0, 0, 0, 8)
                }
                //textView.compoundDrawablePadding = 32
                itemView.colorsLayout.addView(imageView)
            }

           /* val minMaxWattage = itemView.context.getString(
                R.string.description_wattage,
                category.minWattage,
                category.maxWattage,
                category.categoryProductBase
            )


            if (category.minWattage != itemView.context.getString(R.string.no_value)) {
                itemView.product_detail.text = minMaxWattage.replace("-W", "")

            }*/
        }
    }
}
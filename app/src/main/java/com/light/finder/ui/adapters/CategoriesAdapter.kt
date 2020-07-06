package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.*
import com.google.android.material.button.MaterialButton
import com.light.domain.model.Category
import com.light.domain.model.CctType
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_category.view.*


class CategoriesAdapter(
    private val listener: (Category) -> Unit,
    private val filterColorList: List<CctType> = emptyList()
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
            filterColorList: List<CctType>
        ) {
            itemView.category_name.text = category.categoryName

            val sourceString = "<b>" + category.priceRange + "</b> " + "each"
            itemView.priceButton.text = Html.fromHtml(sourceString)

            itemView.bulbCover.loadUrl(category.categoryImage)
            itemView.thumbnail.loadThumbnail(category.categoryImage)
            itemView.bulbName.text = itemView.context.getString(R.string.bulb_s)
                .format(category.categoryShape, category.categoryProducts[0].factorShape)

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

            category.categoryWattReplaced.forEachIndexed { index, watt ->
                val wattButton = Button(itemView.context)
                wattButton.text = "$watt W"
                wattButton.style {
                    add(R.style.WattButton)
                    backgroundRes(R.drawable.button_wattage)
                    layoutMarginEndDp(8)
                }
                itemView.wattageLayout.addView(wattButton)

            }


            category.colors.forEachIndexed { index, colorCode ->
                val imageView = ImageView(itemView.context)
                val drawable = itemView.context.getColorDrawable(colorCode)
                if (drawable != 0) {
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        82,
                        82
                    )
                    imageView.loadThumbnail(
                        getLegendCctTagPrefSmallIcon(
                            colorCode,
                            filterTypeList = filterColorList,
                            legendTag = COLOR_LEGEND_TAG
                        )
                    )
                } else {
                    imageView.setBackgroundResource(R.drawable.ic_placeholder_variation)
                }
                imageView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                if (index < category.colors.size - 1) {
                    imageView.setPadding(0, 0, 32, 0)
                } else if (category.colors.size == 1) {
                    imageView.setPadding(0, 0, 8, 0)
                }
                itemView.colorsLayout.addView(imageView)
            }
        }
    }
}
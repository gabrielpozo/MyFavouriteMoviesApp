package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.backgroundRes
import com.airbnb.paris.extensions.layoutMarginEndDp
import com.airbnb.paris.extensions.style
import com.light.domain.model.Category
import com.light.domain.model.CctType
import com.light.domain.model.FinishType
import com.light.domain.model.FormFactorType
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_category.view.*


class CategoriesAdapter(
    private val listener: (Category) -> Unit,
    private val filterColorList: List<CctType> = emptyList(),
    private val formFactorList: List<FormFactorType> = emptyList(),
    private val filterFinishList: List<FinishType> = emptyList(),
    private val shapeIdentified: String
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
        holder.bind(
            category,
            indexes,
            categories.size,
            position,
            filterColorList,
            formFactorList,
            filterFinishList
        )
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
            filterColorList: List<CctType>,
            formFactorList: List<FormFactorType>,
            finishList: List<FinishType>
        ) {
            itemView.category_name.text = category.categoryName

            val sourceString = "<b>" + category.priceRange + "</b> " + "each"
            itemView.priceButton.text = Html.fromHtml(sourceString)

            itemView.bulbCover.loadUrl(category.categoryImage)

            category.finishCodes.sortSmallFinishByOrderField(finishList).forEachIndexed { index, finishType ->
                val imageViewFinish = ImageView(itemView.context)
                val size = itemView.resources.getDimensionPixelSize(R.dimen.icon_factor)
                imageViewFinish.layoutParams = LinearLayout.LayoutParams(
                    size,
                    size
                )
                imageViewFinish.loadBulbThumbnail(
                    getLegendFormFactorTagPrefSmallIcon(
                        finishType.finnishCode,
                        filterTypeList = finishList,
                        legendTag = FORM_FACTOR_LEGEND_TAG
                    )
                )

                imageViewFinish.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                if (index < category.colors.size - 1) {
                    imageViewFinish.setPadding(0, 0, 4, 0)
                } else if (category.colors.size == 1) {
                    imageViewFinish.setPadding(0, 0, 32, 0)
                }

                itemView.thumbnail.addView(imageViewFinish)
            }

            itemView.bulbName.text = itemView.context.getString(R.string.bulb_s)
                .format(formFactorList[0].name, category.categoryProducts[0].factorShape)

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

            category.colors.sortSmallColorByOrderField(filterColorList)
                .forEachIndexed { index, colorCode ->
                    val imageView = ImageView(itemView.context)
                    val size = itemView.resources.getDimensionPixelSize(R.dimen.icon)
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        size,
                        size
                    )
                    imageView.loadThumbnail(
                        getLegendCctTagPrefSmallIcon(
                            colorCode.cctCode,
                            filterTypeList = filterColorList,
                            legendTag = COLOR_LEGEND_TAG
                        )
                    )

                    imageView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (index < category.colors.size - 1) {
                        imageView.setPadding(0, 0, 4, 0)
                    } else if (category.colors.size == 1) {
                        imageView.setPadding(0, 0, 32, 0)
                    }
                    itemView.colorsLayout.addView(imageView)
                }
        }
    }
}
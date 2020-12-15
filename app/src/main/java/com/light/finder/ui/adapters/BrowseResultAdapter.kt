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
import com.airbnb.paris.extensions.paddingDp
import com.airbnb.paris.extensions.style
import com.light.domain.model.Category
import com.light.domain.model.CctType
import com.light.domain.model.FinishType
import com.light.domain.model.ProductCategoryName
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.browse_results_header.view.*
import kotlinx.android.synthetic.main.item_category.view.*


class BrowseResultAdapter(
    private val listener: (Category) -> Unit,
    private val filterColorList: List<CctType> = emptyList(),
    private val filterFinishList: List<FinishType> = emptyList(),
    private val productCategoryNameList: List<ProductCategoryName> = emptyList(),
    private val shapeIdentified: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val headerOffset: Int = 1

    var categories: List<Category> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.categoryIndex == new.categoryIndex }
    )

    private var isClickable = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                // Here Inflating the header view
                val view = parent.inflate(R.layout.browse_results_header, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEM -> {
                val view = parent.inflate(R.layout.item_category, false)
                ItemViewHolder(view)
            }
            else -> {
                throw ClassCastException("Unknown viewType $viewType")
            }
        }
    }



    fun setAdapterClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged()
    }



    // Size + headerOffset since 0 is reserved for header
    override fun getItemCount(): Int = categories.size + headerOffset


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is HeaderViewHolder) {
            holder.bind(categories.size, shapeIdentified)

        } else if (holder is ItemViewHolder) {
            val category = categories[position - headerOffset]
            val indexes = getMaxIndices(categories)

            // removing view to avoid duplicates
            holder.itemView.colorsLayout.removeAllViews()
            holder.itemView.wattageLayout.removeAllViews()
            holder.itemView.imagesLayout.removeAllViews()
            holder.itemView.thumbnail.removeAllViews()

            holder.bind(
                category,
                indexes,
                categories.size,
                position,
                filterColorList,
                filterFinishList,
                productCategoryNameList
            )
            holder.itemView.setSafeOnClickListener { listener(category) }
            holder.itemView.isClickable = isClickable

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }
        return TYPE_ITEM
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

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            categorySize: Int,
            shapeIdentified: String
        ) {
            when (categorySize) {
                1 -> {
                    itemView.textViewResults.text =
                        itemView.context.getString(R.string.text_result)
                            .getIntFormatter(categorySize)
                }
                else -> {
                    itemView.textViewResults.text =
                        itemView.context.getString(R.string.text_results)
                            .getIntFormatter(categorySize)
                }
            }
            itemView.textViewFitting.text =
                itemView.context.getString(R.string.based_on_result_fitting).format(
                    shapeIdentified
                )

        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")

        fun bind(
            category: Category,
            indexes: List<Int>,
            categoriesSize: Int,
            position: Int,
            filterColorList: List<CctType>,
            finishList: List<FinishType>,
            productNameCategoryList: List<ProductCategoryName>
        ) {

            itemView.category_name.text = getLegendCategoryName(
                category.categoryProducts[0].produtCategoryCode,
                productNameCategoryList
            )

            val sourceString = "<b>" + category.priceRange + "</b> " + "each"
            itemView.priceButton.text = Html.fromHtml(sourceString)

            itemView.bulbCover.loadUrl(
                category.categoryProducts.getProductImage()
            )

            category.finishCodes.sortSmallFinishByOrderField(finishList)
                .forEachIndexed { index, finishType ->
                    val imageViewFinish = ImageView(itemView.context)
                    val size = itemView.resources.getDimensionPixelSize(R.dimen.icon_factor)
                    val params = LinearLayout.LayoutParams(
                        size,
                        size
                    )
                    params.setMargins(0, 0, 12, 0)
                    imageViewFinish.layoutParams = params

                    imageViewFinish.style {
                        backgroundRes(R.drawable.all_circle)
                        paddingDp(1)
                    }
                    imageViewFinish.loadBulbThumbnail(
                        getLegendFormFactorTagPrefSmallIcon(
                            finishType.finnishCode,
                            filterTypeList = finishList,
                            legendTag = FORM_FACTOR_LEGEND_TAG
                        )
                    )


                    if (index < category.colors.size - 1) {
                        imageViewFinish.setPadding(2, 2, 2, 2)
                    } else if (category.colors.size == 1) {
                        imageViewFinish.setPadding(2, 2, 2, 2)
                    }

                    itemView.thumbnail.addView(imageViewFinish)
                }

            itemView.bulbName.text = itemView.context.getString(R.string.bulb_s)
                .format(
                    category.categoryShape, category.categoryProducts[0].factorShape
                )

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

            category.categoryWattReplaced.sortedBy { it }.forEachIndexed { index, watt ->
                val wattButton = Button(itemView.context)
                wattButton.text = "$watt W"
                wattButton.style {
                    add(R.style.WattButton)
                    backgroundRes(R.drawable.button_wattage)
                    layoutMarginEndDp(8)
                }
                itemView.wattageLayout.addView(wattButton)

            }

            //TODO here we get all the differenct numbers

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
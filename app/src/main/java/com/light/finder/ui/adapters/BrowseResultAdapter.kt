package com.light.finder.ui.adapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.*
import com.light.domain.model.*
import com.light.finder.R
import com.light.finder.extensions.*
import kotlinx.android.synthetic.main.item_category.view.*


class BrowseResultAdapter(
    private val listener: (Category) -> Unit,
    private val filterColorList: List<CctType> = emptyList(),
    private val formFactorList: List<FormFactorType> = emptyList(),
    private val filterFinishList: List<FinishType> = emptyList(),
    private val formFactorIdList: List<FormFactorTypeId> = emptyList(),
    private val productCategoryNameList: List<ProductCategoryName> = emptyList(),
    private val shapeIdentified: String
) :
    RecyclerView.Adapter<BrowseResultAdapter.ViewHolder>() {

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
            formFactorIdList,
            shapeIdentified,
            filterFinishList,
            productCategoryNameList
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
            formFactorListId: List<FormFactorTypeId>,
            shapeIdentified: String,
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
                getLegendCategoryImage(
                    category.categoryProducts[0].produtCategoryCode,
                    productNameCategoryList
                )
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
package com.light.repository

import com.light.domain.BrowseChoiceRepository
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ProductBrowsing
import com.light.domain.model.ShapeBrowsing
import com.light.source.local.LocalPreferenceDataSource

class BrowseChoiceCategoryRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    BrowseChoiceRepository {
    override fun getCategoryCategoriesChoice(browsingList: List<ShapeBrowsing>): List<ChoiceBrowsing> {
        val categoryChoiceList = mutableListOf<ChoiceBrowsing>()
        val categoryNameList = localPreferenceDataSource.loadProductCategoryName()
        val productBrowsingList = localPreferenceDataSource.loadProductBrowsingFiltered()

        categoryNameList.forEach { categoryName ->
            categoryChoiceList.add(
                ChoiceBrowsing(
                    id = categoryName.id,
                    name = categoryName.name,
                    order = categoryName.order,
                    image = categoryName.image,
                    description = categoryName.description,
                    subtitleCount = checkCat(
                        categoryName.id,
                        localPreferenceDataSource.getShapeFilteredList(browsingList)
                    )
                )
            )


        }
         return categoryChoiceList
    }
}

private fun checkCat(
    categoryId: Int,
    productBrowsingList: List<ProductBrowsing>
): Int {

    productBrowsingList.groupBy {productBrowsing ->
     //   productBrowsing.toKeyCaetegoryChoice()
    }
    return 0
}

//fun ProductBrowsing.toKeyCaetegoryChoice() = Key(productCategoryCode)
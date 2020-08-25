package com.light.repository

import com.light.domain.BrowseChoiceRepository
import com.light.domain.model.*
import com.light.domain.toKey
import com.light.source.local.LocalPreferenceDataSource

class BrowseChoiceCategoryRepositoryImpl(private val localPreferenceDataSource: LocalPreferenceDataSource) :
    BrowseChoiceRepository {
    override fun getCategoryCategoriesChoice(browsingList: List<ShapeBrowsing>): List<ChoiceBrowsing> {
        val categoryChoiceList = mutableListOf<ChoiceBrowsing>()
        val categoryNameList = localPreferenceDataSource.loadProductCategoryName()
        val filteredProductsByShape = localPreferenceDataSource.getShapeFilteredList(browsingList)

        //TODO maybe save the new product filtered list here??, probably
        localPreferenceDataSource.saveFittingFilteredList(filteredProductsByShape)

        categoryNameList.forEach { categoryName ->
            categoryChoiceList.add(
                ChoiceBrowsing(
                    id = categoryName.id,
                    name = categoryName.name,
                    order = categoryName.order,
                    image = categoryName.image,
                    description = categoryName.description,
                    baseNameFitting = browsingList[0].baseNameFitting,
                    subtitleCount = filteredProductsByShape.filter {
                        categoryName.id == it.productCategoryCode
                    }.groupBy { it.toKey() }.size
                )
            )
        }
        return categoryChoiceList
    }
}


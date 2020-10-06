package com.light.usecases

import com.light.domain.model.Category

@Suppress("UNCHECKED_CAST")
class GetSortCategoriesUseCase {
    suspend fun executeSorting(
        sortId: Int,
        categories: List<Category>
    ): List<Category> {

        val sortedCategories = categories.map { it.copy() }
        val newCategories = mutableListOf<Category>()

        when (sortId) {
            Sort.RECOMMENDED.id -> {
                newCategories.addAll(sortedCategories.sortedBy { it.categoryIndex })
                return newCategories
            }
            Sort.MAX.id -> {
                //todo check sorting
                newCategories.addAll(
                    categories.sortedBy { category ->
                        category.categoryProducts.maxBy { it.priceLamp }?.priceLamp
                    })
                return newCategories
            }
            Sort.MIN.id -> {
                //todo check sorting
                newCategories.addAll(
                    categories.sortedBy { category ->
                        category.categoryProducts.minBy { it.priceLamp }?.priceLamp
                    })
                return newCategories
            }
            else -> {
                newCategories.addAll(
                    sortedCategories.sortedBy { it.categoryIndex })
                return newCategories
            }
        }
    }
}

enum class Sort(val id: Int) {
    RECOMMENDED(2131362146),
    MIN(2131362092),
    MAX(2131362030)
}

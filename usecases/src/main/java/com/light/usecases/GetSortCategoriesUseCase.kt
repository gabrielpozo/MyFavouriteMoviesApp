package com.light.usecases

import com.light.domain.model.Category

@Suppress("UNCHECKED_CAST")
class GetSortCategoriesUseCase {
    suspend fun executeSorting(
        sortId: Int,
        categories: List<Category>
    ): List<Category> {

        val sortedCategories = categories.map { it.copy() }

        return when (sortId) {
            Sort.RECOMMENDED.id -> {
                sortedCategories.sortedBy { it.categoryIndex }
            }
            Sort.MAX.id -> {
                //todo change dto
                categories.sortedBy {
                    it.priceRange.substringAfter("-").replace("$", "").toDouble()
                }
            }
            Sort.MIN.id -> {
                categories.sortedBy {
                    it.priceRange.substringBefore("-").replace("$", "").toDouble()
                }
            }
            else -> {
                sortedCategories.sortedBy { it.categoryIndex }
            }
        }
    }
}

enum class Sort(val id: Int) {
    RECOMMENDED(2131362146),
    MIN(2131362092),
    MAX(2131362030)
}

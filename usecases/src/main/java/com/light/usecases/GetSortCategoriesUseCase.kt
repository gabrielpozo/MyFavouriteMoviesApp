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
                categories.sortedBy { it.minPrice }.reversed()
            }
            Sort.MIN.id -> {
                categories.sortedBy { it.minPrice }
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
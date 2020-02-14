package com.accenture.usecases

import com.accenture.domain.model.Filter
import com.accenture.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetActiveFiltersUseCase : BaseUseCase<List<Filter>>() {


    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
        val setActiveButtonsList = params[0] as List<Filter>
        val initFilterList = params[1] as List<Filter>

        val activeOnInitList = initFilterList.filter { it.isActive }

        activeOnInitList.map { filterOnInitList ->
            setActiveButtonsList.find { filterOnInitList.nameFilter == it.nameFilter }?.isActive =
                true
        }

        return DataState.Success(
            removeDuplicateTypeElements(
                setActiveButtonsList.toMutableList(),
                activeOnInitList
            )
        )

    }


    private fun removeDuplicateTypeElements(
        filterList: MutableList<Filter>,
        activeOnInitList: List<Filter>
    ): List<Filter> {
        filterList.map { filter ->
            val num = filterList.groupBy { it.type == filter.type }.count()
            if (num == 1 && !activeOnInitList.contains(filter)) {
                filterList.remove(filter)
            }
        }

        return filterList
    }

}
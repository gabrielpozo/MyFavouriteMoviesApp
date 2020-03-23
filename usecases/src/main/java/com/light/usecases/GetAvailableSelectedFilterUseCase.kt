package com.light.usecases

import com.light.domain.model.FilterWattage
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetAvailableSelectedFilterUseCase : BaseUseCase<List<FilterWattage>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterWattage>> {
        val filterList: List<FilterWattage> = params[0] as List<FilterWattage>
        val filter: FilterWattage = params[1] as FilterWattage

        val filterListManipulated = filterList.toMutableList()

        filterListManipulated.find { filterWattage ->
            filterWattage.isSelected

        }.apply {
            this?.isSelected = false
        }

        filterListManipulated.find { filterWattage ->
            filterWattage.nameFilter == filter.nameFilter

        }?.apply {  nameFilter = "92"}

        return DataState.Success(filterListManipulated.toList())
    }

}
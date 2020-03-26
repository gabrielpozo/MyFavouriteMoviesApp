package com.light.usecases

import com.light.domain.model.FilterVariation
import com.light.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetAvailableSelectedFilterUseCase : BaseUseCase<List<FilterVariation>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<FilterVariation>> {
        val filterList: List<FilterVariation> = params[0] as List<FilterVariation>
        val filter: FilterVariation = params[1] as FilterVariation

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
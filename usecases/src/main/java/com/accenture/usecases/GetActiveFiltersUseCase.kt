package com.accenture.usecases

import com.accenture.domain.model.Filter
import com.accenture.domain.state.DataState


@Suppress("UNCHECKED_CAST")
class GetActiveFiltersUseCase : BaseUseCase<List<Filter>>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*   override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Filter>> {
           val filterButtonsNoActive = params[0] as List<Filter>
           val initFilterList = params[1] as List<Filter>
           val activeOnInitList = initFilterList.filter { it.isActive }

           activeOnInitList.map { filterOnInitList ->
               filterButtonsNoActive.find { filterOnInitList.nameFilter == it.nameFilter }?.isActive =
                   true
           }

           return DataState.Success(filterButtonsNoActive.removeDuplicateElements(activeOnInitList))

       }*/
    
}
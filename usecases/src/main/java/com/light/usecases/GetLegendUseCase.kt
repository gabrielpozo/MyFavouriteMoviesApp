package com.light.usecases

import com.light.domain.LegendRepository
import com.light.domain.model.LegendParsing
import com.light.domain.state.DataState


class GetLegendUseCase(private val legendRepository: LegendRepository) {
    suspend fun execute(
        onSuccess: (LegendParsing) -> Unit = {},
        onError: (message: String) -> Unit = { _-> }
    ) {
        when (val dataState = legendRepository.getLegendTags()) {
            is DataState.Success -> {
                onSuccess.invoke(dataState.data)
            }
            is DataState.Error -> {
                onError.invoke(dataState.errorMessage)
            }
        }
    }
}
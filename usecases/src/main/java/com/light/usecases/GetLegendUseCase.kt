package com.light.usecases

import com.light.domain.LegendRepository
import com.light.domain.state.DataState


class GetLegendUseCase(private val legendRepository: LegendRepository) {
    suspend fun execute(
        onSuccess: () -> Unit = {},
        onError: (e: Exception, message: String) -> Unit = { _, _ -> }
    ) {
        when (val dataState = legendRepository.getLegendTags()) {
            is DataState.Success -> {
                onSuccess.invoke()
            }
            is DataState.Error -> {
                onError.invoke(dataState.cause ?: Exception(""), dataState.errorMessage)
            }
        }
    }
}
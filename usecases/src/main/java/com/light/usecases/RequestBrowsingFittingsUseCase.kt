package com.light.usecases

import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.state.DataState


class RequestBrowsingFittingsUseCase(private val browseLightBulbsRepository: BrowseLightBulbsRepository) {
    suspend fun execute(
        onSuccess: (List<FormFactorTypeBaseId>) -> Unit = {},
        onError: (e: Exception, message: String) -> Unit = { _, _ -> }
    ) {
        when (val dataState = browseLightBulbsRepository.getFittingBrowsingProducts()) {
            is DataState.Success -> {
                onSuccess.invoke(dataState.data)
            }
            is DataState.Error -> {
                onError.invoke(dataState.cause ?: Exception(""), dataState.errorMessage)
            }
            is DataState.TimeOut -> {
                onError.invoke(dataState.cause ?: Exception(""), dataState.errorMessage)
            }
        }
    }

}
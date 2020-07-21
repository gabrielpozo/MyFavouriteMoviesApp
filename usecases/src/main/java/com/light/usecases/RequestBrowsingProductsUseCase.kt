package com.light.usecases

import com.light.domain.BrowseLightBulbsRepository
import com.light.domain.model.FittingBrowsing
import com.light.domain.state.DataState


class RequestBrowsingProductsUseCase(private val browseLightBulbsRepository: BrowseLightBulbsRepository) {
    suspend fun execute(
        onSuccess: (List<FittingBrowsing>) -> Unit = {},
        onError: (e: Exception, message: String) -> Unit = { _, _ -> }
    ) {
        when (val dataState = browseLightBulbsRepository.getBrowsingProducts()) {
            is DataState.Success -> {
                onSuccess.invoke(dataState.data)
            }
            is DataState.Error -> {
                onError.invoke(dataState.cause ?: Exception(""), dataState.errorMessage)
            }
        }
    }

}
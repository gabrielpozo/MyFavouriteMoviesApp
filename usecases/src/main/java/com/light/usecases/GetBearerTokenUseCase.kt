package com.light.usecases

import com.light.domain.AuthRepository
import com.light.domain.state.DataState

class GetBearerTokenUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(
        onSuccess: () -> Unit = {},
        onError: (e: Exception, message: String) -> Unit = { _, _ -> }
    ) {
        when (val dataState = authRepository.getBearerToken()) {
            is DataState.Success -> {
                onSuccess.invoke()
            }
            is DataState.Error -> {
                onError.invoke(dataState.cause ?: Exception(""), dataState.errorMessage)
            }
        }
    }
}
package com.light.usecases

import com.light.domain.TermsAndConditionsRepository
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class AddToSharedPrefUseCase(private val isAccepted: Boolean) : BaseUseCase<Boolean>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<Boolean> {
        return TermsAndConditionsRepository.addToSharedPref(isAccepted)
    }
}
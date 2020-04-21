package com.light.domain

import com.light.domain.state.DataState


interface TermsAndConditionsRepository {
    suspend fun addToSharedPref(isAccepted: Boolean): DataState<Boolean>
}
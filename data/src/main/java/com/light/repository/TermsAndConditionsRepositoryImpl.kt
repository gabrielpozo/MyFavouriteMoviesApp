package com.light.repository

import com.light.domain.TermsAndConditionsRepository
import com.light.domain.state.DataState
import com.light.source.local.LocalSharedPrefDataSource

class TermsAndConditionsRepositoryImpl(private val localSharedPrefDataSource: LocalSharedPrefDataSource) : TermsAndConditionsRepository {
    override suspend fun addToSharedPref(isAccepted: Boolean): DataState<Boolean> {
        return DataState.Success(localSharedPrefDataSource.addToSharedPRef(isAccepted))
    }


}
package com.light.domain

import com.light.domain.model.Credential
import com.light.domain.state.DataState


interface AuthRepository {
    suspend fun getBearerToken(): DataState<Credential>
}
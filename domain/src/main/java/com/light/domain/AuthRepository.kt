package com.light.domain

import com.light.domain.model.Bearer
import com.light.domain.state.DataState


interface AuthRepository {
    suspend fun getBearerToken(): DataState<Bearer>
}
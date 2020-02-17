package com.accenture.domain

import com.accenture.domain.model.Message
import com.accenture.domain.state.DataState

interface CategoryRepository {

    suspend fun getMessage(base64: String?): DataState<List<Message>>
}
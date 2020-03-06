package com.light.domain

import com.light.domain.model.Message
import com.light.domain.state.DataState

interface CategoryRepository {

    suspend fun getMessage(base64: String?): DataState<List<Message>>
    suspend fun getFileImagePathEncoded(absolutePath: String): DataState<String>
}
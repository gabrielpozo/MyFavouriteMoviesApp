package com.accenture.usecases

import com.accenture.domain.CategoryRepository
import com.accenture.domain.model.Message
import com.accenture.domain.model.Product
import com.accenture.domain.state.DataState


class GetCategoriesResultUseCase(private val categoryRepository: CategoryRepository) :
    BaseUseCase<List<Message>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Message>> {
        val base64: String = params[0] as String
        return categoryRepository.getMessage(base64)
    }
}
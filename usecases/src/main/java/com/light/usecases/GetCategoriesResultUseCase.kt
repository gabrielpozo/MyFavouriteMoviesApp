package com.light.usecases

import com.light.domain.CategoryRepository
import com.light.domain.model.Message
import com.light.domain.state.DataState


class GetCategoriesResultUseCase(private val categoryRepository: CategoryRepository) :
    BaseUseCase<List<Message>>() {

    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<List<Message>> {
        val base64: String = params[0] as String
        return categoryRepository.getMessage(base64)
    }
}
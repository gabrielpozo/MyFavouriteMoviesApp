package com.accenture.usecases

import com.accenture.domain.CategoryRepository
import com.accenture.domain.model.Message
import com.accenture.domain.state.DataState


class GetCategoriesResultUseCase(private val categoryRepository: CategoryRepository) :
    BaseUseCase<List<Message>, Any>() {

    override suspend fun useCaseExecution(params: Any?): DataState<List<Message>> =
        categoryRepository.getMessage(params.toString())
}
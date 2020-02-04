package com.accenture.usecases

import com.accenture.domain.CategoryRepository
import com.accenture.domain.model.Message
import com.accenture.domain.state.DataState


class GetCategoryResultUseCase(private val categoryRepository: CategoryRepository) :
    BaseUseCase<Message, Any>() {

    override suspend fun useCaseExecution(params: Any?): DataState<Message> =
        categoryRepository.getMessage()


}
package com.light.usecases

import com.light.domain.CategoryRepository
import com.light.domain.state.DataState


class GetFilePathImageEncodedUseCase(private val categoryRepository: CategoryRepository) :
    BaseScanningUseCase<String>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<String> {
        val absolutePath: String = params[0] as String
        return categoryRepository.getFileImagePathEncoded(absolutePath)
    }
}
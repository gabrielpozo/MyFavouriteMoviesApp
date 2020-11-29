package com.gabriel.usecases

import com.gabriel.domain.Resource
import com.gabriel.domain.ResourceException
import com.gabriel.domain.ResourceStatus

abstract class MoviesBaseUseCase<T> {

    suspend fun execute(
        onSuccess: (T) -> Unit,
        onError: (ResourceException?) -> Unit = {}
    ) {
        val resource = useCaseExecution()
        when (resource.state) {
            ResourceStatus.SUCCESS -> {
                resource.value?.apply {
                    onSuccess.invoke(this)
                } ?: onError.invoke(null)
            }
            ResourceStatus.ERROR -> onError.invoke(resource.error)
        }
    }

    protected abstract suspend fun useCaseExecution(): Resource<T>
}
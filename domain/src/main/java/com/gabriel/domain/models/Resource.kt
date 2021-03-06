package com.gabriel.domain


class Resource<ResultType>(
    var state: ResourceStatus? = ResourceStatus.ERROR,
    var value: ResultType? = null,
    var error: ResourceException? = null
) {

    companion object {
        fun <ResultType> success(data: ResultType): Resource<ResultType> = Resource(ResourceStatus.SUCCESS, data)

        fun <ResultType> error(error: ResourceException?): Resource<ResultType> =
            Resource(ResourceStatus.ERROR, error = error)
    }
}

enum class ResourceStatus {
    SUCCESS,
    ERROR
}
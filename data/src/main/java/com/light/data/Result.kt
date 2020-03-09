package com.light.data

data class Result<out T>(
    val status: Status,
    val data: T? = null,
    val message: String?,
    val isCancelRequest: Boolean = false
) {

    enum class Status {
        SUCCESS,
        ERROR,
        EMPTY,
        CANCELED
    }

    companion object {
        fun <T> success(data: T? = null, hasContent: Boolean = true): Result<T> {
            return Result(
                if (hasContent) {
                    Status.SUCCESS
                } else {
                    Status.EMPTY
                },
                data,
                null
            )
        }


        fun <T> error(
            message: String,
            data: T? = null,
            isCancelRequest: Boolean = false
        ): Result<T> {
            return Result(
                if (!isCancelRequest) {
                    Status.ERROR
                } else {
                    Status.CANCELED
                },
                data,
                message
            )
        }
    }
}


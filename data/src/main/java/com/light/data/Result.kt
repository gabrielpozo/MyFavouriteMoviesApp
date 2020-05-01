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
        TIME_OUT,
        PARSE_ERROR
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
            isTimeout: Boolean = false,
            isCanceled: Boolean = false,
            isParseError: Boolean = false
        ): Result<T> {
            return Result(
                when {
                    isTimeout -> {
                        Status.TIME_OUT
                    }
                    isParseError -> {
                        Status.PARSE_ERROR
                    }
                    else -> {
                        Status.ERROR
                    }
                },
                data,
                message,
                isCanceled
            )
        }
    }
}


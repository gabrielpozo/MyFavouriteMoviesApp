package com.light.data

data class Result<out T>(
    val status: Status,
    val data: T? = null,
    val message: String?,
    val isCancelRequest: Boolean = false,
    val code: Int = 0

) {

    enum class Status {
        SUCCESS,
        ERROR,
        BAD_REQUEST,
        TIME_OUT_ERROR,
        PARSE_ERROR
    }

    companion object {
        fun <T> success(data: T? = null, code: Int = 200): Result<T> {
            return Result(
                Status.SUCCESS,
                data,
                code = code,
                message = null
            )
        }

        fun <T> badRequest(data: T? = null, code: Int = 404): Result<T> {
            return Result(
                Status.BAD_REQUEST,
                data,
                code = code,
                message = null
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
                        Status.TIME_OUT_ERROR
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


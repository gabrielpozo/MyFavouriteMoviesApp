package com.light.finder.data.source

import com.light.data.Result
import com.light.util.*


abstract class BaseCartDataSource <Dto, DomainModel> : BaseDataSource<Dto, DomainModel>() {

    override fun badRequestResponse(code: Int): Result<DomainModel> =
        when (code) {
            PRODUCT_NOT_FOUND -> {
                Result.badRequest(code = PRODUCT_NOT_FOUND)
            }
            OUT_STOCK -> {
                Result.badRequest(code = OUT_STOCK)
            }
            PRODUCT_DISABLE -> {
                Result.badRequest(code = PRODUCT_DISABLE)
            }
            else -> {
                Result.badRequest(code = PRODUCT_NOT_FOUND)
            }
        }
}





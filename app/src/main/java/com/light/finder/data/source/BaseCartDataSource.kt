package com.light.finder.data.source

import com.light.data.Result
import com.light.util.*
import retrofit2.Response

abstract class BaseCartDataSource <Dto, DomainModel> : BaseDataSource<Dto, DomainModel>() {

    override fun successfulResponse(response: Response<Dto>): Result<DomainModel> =
        when (response.code()) {
            SUCCESSFUL_CODE -> {
                Result.success(mapResultToDomainModel(response.body()!!), code = SUCCESSFUL_CODE)
            }
            else -> {
                Result.success(mapResultToDomainModel(response.body()!!), code = response.code())
            }
        }


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





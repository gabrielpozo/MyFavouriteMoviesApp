package com.light.finder.data.source

import com.light.data.Result
import com.light.util.SUCCESSFUL_CODE
import retrofit2.Response

abstract class BaseCatalogueDataSource <Dto, DomainModel> : BaseDataSource<Dto, DomainModel>() {

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
        Result.badRequest(code = code)

}
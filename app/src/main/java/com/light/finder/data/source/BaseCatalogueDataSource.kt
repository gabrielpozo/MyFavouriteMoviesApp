package com.light.finder.data.source

import com.light.data.Result
import com.light.util.SUCCESSFUL_CODE
import retrofit2.Response

abstract class BaseCatalogueDataSource <Dto, DomainModel> : BaseDataSource<Dto, DomainModel>() {

    override fun successfulResponse(response: Response<Dto>, code: Int): Result<DomainModel> =
        when (code) {
            SUCCESSFUL_CODE -> {
                Result.success(mapResultToDomainModel(response.body()!!), code = SUCCESSFUL_CODE)
            }
            else -> {
                Result.success(mapResultToDomainModel(response.body()!!))
            }
        }


    override fun badRequestResponse(code: Int): Result<DomainModel> =
        Result.badRequest(code = code)

}
package com.light.finder.data.source

import com.light.data.Result
import com.light.domain.model.Message
import com.light.finder.data.mappers.mapServerMessagesToDomain
import com.light.finder.data.source.remote.dto.CategoryResultDto
import com.light.util.*
import retrofit2.Response

abstract class BaseScanningDataSource : BaseDataSource<CategoryResultDto, List<Message>>() {

    override fun successfulResponse(
        response: Response<CategoryResultDto>
    ): Result<List<Message>> =
        when (response.code()) {
            SUCCESSFUL_CODE -> {
                Result.success(mapResultToDomainModel(response.body()!!),code = SUCCESSFUL_CODE)
            }
            NO_CONTENT_CODE -> {
                Result.success(code = NO_CONTENT_CODE)
            }
            NO_PRODUCTS_CODE -> {
                Result.success(mapResultToDomainModel(response.body()!!), code = NO_PRODUCTS_CODE)
            }
            else -> {
                Result.success(mapResultToDomainModel(response.body()!!), code = response.code())
            }
        }


    override fun badRequestResponse(code: Int): Result<List<Message>> =
        Result.badRequest(code = code)


    override fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> {
        return categoryResult.messageList?.map(mapServerMessagesToDomain)!!
    }
}

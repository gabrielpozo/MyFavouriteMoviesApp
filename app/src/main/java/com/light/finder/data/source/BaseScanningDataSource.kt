package com.light.finder.data.source

import com.light.data.Result
import com.light.domain.model.Message
import com.light.finder.data.mappers.mapServerMessagesToDomain
import com.light.finder.data.source.remote.dto.CategoryResultDto

abstract class BaseScanningDataSource : BaseDataSource<CategoryResultDto, List<Message>>() {

    override fun badRequestResponse(code: Int): Result<List<Message>> =
        Result.badRequest(code = code)


    override fun mapResultToDomainModel(categoryResult: CategoryResultDto): List<Message> {
        return categoryResult.messageList?.map(mapServerMessagesToDomain)!!
    }
}

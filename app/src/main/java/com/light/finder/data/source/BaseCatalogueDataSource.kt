package com.light.finder.data.source

import com.light.data.Result

abstract class BaseCatalogueDataSource <Dto, DomainModel> : BaseDataSource<Dto, DomainModel>() {

    override fun badRequestResponse(code: Int): Result<DomainModel> =
        Result.badRequest(code = code)

}
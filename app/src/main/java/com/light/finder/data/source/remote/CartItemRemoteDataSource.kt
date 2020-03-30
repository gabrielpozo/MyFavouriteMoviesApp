package com.light.finder.data.source.remote

import com.light.data.Result
import com.light.domain.model.Cart
import com.light.finder.data.mappers.mapCartToDomain
import com.light.source.remote.BaseDataSource
import com.light.source.remote.CartRemoteDataSource

class CartItemRemoteDataSource : BaseDataSource(),
    CartRemoteDataSource {

    override suspend fun fetchCartItems(productSapId: String): Result<Cart> =
        getResult(::mapResultToDomainModel) {
            CartRemoteUtil.service.fetchCartAsync(productSapId)
        }

    private fun mapResultToDomainModel(cartResult: CartResultDto): Cart {
        return mapCartToDomain(cartResult)
    }
}
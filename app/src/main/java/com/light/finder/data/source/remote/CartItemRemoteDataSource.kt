package com.light.finder.data.source.remote

import android.content.Context
import com.light.data.Result
import com.light.domain.model.Cart
import com.light.finder.data.mappers.mapCartToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.dto.CartResultDto
import com.light.finder.data.source.remote.services.CartRemoteUtil
import com.light.source.remote.CartRemoteDataSource

class CartItemRemoteDataSource(val context: Context) : BaseDataSource(),
    CartRemoteDataSource {

    override suspend fun fetchCartItems(productSapId: String): Result<Cart> =
        getResult(::mapResultToDomainModel) {
            CartRemoteUtil.getInstance(context).service.fetchCartAsync(productSapId)
        }

    private fun mapResultToDomainModel(cartResult: CartResultDto): Cart {
        return mapCartToDomain(cartResult)
    }
}
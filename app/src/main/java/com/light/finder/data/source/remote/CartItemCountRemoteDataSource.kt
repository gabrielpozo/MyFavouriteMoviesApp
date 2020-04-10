package com.light.finder.data.source.remote

import android.content.Context
import com.light.data.Result
import com.light.domain.model.CartItemCount
import com.light.finder.data.mappers.mapCartItemCountToDomain
import com.light.source.remote.BaseDataSource
import com.light.source.remote.ItemRemoteDataSource

class CartItemCountRemoteDataSource(val context: Context) : BaseDataSource(), ItemRemoteDataSource {
    override suspend fun fetchCartItemCount(): Result<CartItemCount> =
        getResult(::mapResultToDomainModel) {
            CartRemoteUtil.getInstance(context).service.fetchCartItemCountAsync()
        }

    private fun mapResultToDomainModel(cartItemCountResult: CartItemCountResultDto): CartItemCount {
        return mapCartItemCountToDomain(cartItemCountResult)
    }

}
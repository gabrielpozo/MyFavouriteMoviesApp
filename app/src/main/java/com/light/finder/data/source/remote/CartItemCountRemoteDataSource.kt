package com.light.finder.data.source.remote

import android.content.Context
import com.light.data.Result
import com.light.domain.model.CartItemCount
import com.light.finder.data.mappers.mapCartItemCountToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.services.CartRemoteUtil
import com.light.source.remote.ItemRemoteDataSource

class CartItemCountRemoteDataSource(val context: Context) :
    BaseDataSource<CartItemCountResultDto, CartItemCount>(), ItemRemoteDataSource {
    override suspend fun fetchCartItemCount(): Result<CartItemCount> =
        getResult {
            CartRemoteUtil.getInstance(context).service.fetchCartItemCountAsync()
        }

    override fun mapResultToDomainModel(cartItemCountResult: CartItemCountResultDto): CartItemCount =
        mapCartItemCountToDomain(cartItemCountResult)


}
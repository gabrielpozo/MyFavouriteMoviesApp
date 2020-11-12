package com.light.finder.data.source.remote

import android.content.Context
import com.light.data.Result
import com.light.domain.model.CartItemCount
import com.light.finder.data.mappers.mapCartItemCountToDomain
import com.light.finder.data.source.BaseDataSource
import com.light.finder.data.source.remote.services.CartRemoteUtil
import com.light.source.remote.ItemRemoteDataSource
import com.light.util.CART_FLAG_TIMEOUT
import kotlinx.coroutines.withTimeout

class CartItemCountRemoteDataSource(val context: Context) :
    BaseDataSource<CartItemCountResultDto, CartItemCount>(), ItemRemoteDataSource {
    override suspend fun fetchCartItemCount(): Result<CartItemCount> =
        withTimeout(CART_FLAG_TIMEOUT) {
            getResult {
                CartRemoteUtil.getInstance(context).service.fetchCartItemCountAsync()
            }
        }

    override fun mapResultToDomainModel(cartItemCountResult: CartItemCountResultDto): CartItemCount =
        mapCartItemCountToDomain(cartItemCountResult)


}
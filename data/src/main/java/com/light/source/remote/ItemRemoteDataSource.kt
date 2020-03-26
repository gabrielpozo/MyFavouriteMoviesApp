package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.CartItemCount

interface ItemRemoteDataSource {
    suspend fun fetchCartItemCount(): Result<CartItemCount>
}
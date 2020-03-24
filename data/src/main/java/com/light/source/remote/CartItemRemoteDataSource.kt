package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.Cart
import com.light.domain.model.Message

interface CartRemoteDataSource {
    suspend fun fetchCartItems(productSapId: String): Result<Cart>
}
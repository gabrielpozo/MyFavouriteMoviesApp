package com.light.source.remote

import com.light.data.Result
import com.light.domain.model.Cart

interface CartRemoteDataSource {
    suspend fun fetchCartItems(productSapId: String): Result<Cart>
}
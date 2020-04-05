package com.light.domain

import com.light.domain.model.Cart
import com.light.domain.state.DataState

interface CartRepository {
    suspend fun getCartItem(productSapId: String?): DataState<Cart>
}
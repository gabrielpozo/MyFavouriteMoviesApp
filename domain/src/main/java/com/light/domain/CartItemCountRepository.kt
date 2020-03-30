package com.light.domain

import com.light.domain.model.CartItemCount
import com.light.domain.state.DataState

interface CartItemCountRepository {
    suspend fun getCartItemCount(): DataState<CartItemCount>
}
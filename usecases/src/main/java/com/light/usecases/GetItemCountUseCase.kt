package com.light.usecases

import com.light.domain.CartItemCountRepository
import com.light.domain.model.CartItemCount
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetItemCountUseCase(private val cartItemCartRepository: CartItemCountRepository) :
    BaseUseCase<CartItemCount>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<CartItemCount> {
        return cartItemCartRepository.getCartItemCount()
    }
}
package com.light.usecases

import com.light.domain.CartRepository
import com.light.domain.model.Cart
import com.light.domain.state.DataState

@Suppress("UNCHECKED_CAST")
class GetAddToCartUseCase(private val cartRepository: CartRepository) : BaseCartUseCase<Cart>() {
    override suspend fun useCaseExecution(params: Array<out Any?>): DataState<Cart> {
        val productSapId = params[0] as String
        return cartRepository.getCartItem(productSapId)
    }
}
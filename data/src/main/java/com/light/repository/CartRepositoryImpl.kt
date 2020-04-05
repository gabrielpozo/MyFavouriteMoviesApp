package com.light.repository

import com.light.data.repositoryHandleSource
import com.light.domain.CartRepository
import com.light.domain.model.Cart
import com.light.domain.state.DataState
import com.light.source.remote.CartRemoteDataSource

class CartRepositoryImpl(
    private val itemRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun getCartItem(productSapId: String?): DataState<Cart> {
        return repositoryHandleSource(remoteSourceRequest = { itemRemoteDataSource.fetchCartItems(productSapId!!) })
    }


}
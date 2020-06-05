package com.light.repository

import com.light.data.repositoryCartHandleSource
import com.light.domain.CartItemCountRepository
import com.light.domain.model.CartItemCount
import com.light.domain.state.DataState
import com.light.source.remote.ItemRemoteDataSource

class CartItemCountRepositoryImpl(
    private val itemRemoteDataSource: ItemRemoteDataSource
) : CartItemCountRepository {
    override suspend fun getCartItemCount(): DataState<CartItemCount> {
        return repositoryCartHandleSource(remoteSourceRequest = { itemRemoteDataSource.fetchCartItemCount() })
    }


}
package com.light.finder.di.modules

import com.light.domain.CartRepository
import com.light.domain.CategoryRepository
import com.light.repository.CartRepositoryImpl
import com.light.repository.CategoryRepositoryImpl
import com.light.source.local.LocalMediaDataSource
import com.light.source.remote.CartRemoteDataSource
import com.light.source.remote.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun getApiRepository(
        remoteDataSource: RemoteDataSource,
        localMediaDataSource: LocalMediaDataSource
    ): CategoryRepository =
        CategoryRepositoryImpl(remoteDataSource, localMediaDataSource)

    @Provides
    fun getCartRepository(
        itemRemoteDataSource: CartRemoteDataSource
    ): CartRepository =
        CartRepositoryImpl(itemRemoteDataSource)
}
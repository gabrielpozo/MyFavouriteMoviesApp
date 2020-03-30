package com.light.finder.di.modules

import com.light.finder.data.source.local.LocalMediaDataSourceImpl
import com.light.finder.data.source.remote.CartItemCountRemoteDataSource
import com.light.finder.data.source.remote.CartItemRemoteDataSource
import com.light.finder.data.source.remote.SignifyRemoteDataSource
import com.light.source.local.LocalMediaDataSource
import com.light.source.remote.CartRemoteDataSource
import com.light.source.remote.ItemRemoteDataSource
import com.light.source.remote.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun getRemoteDataSource(): RemoteDataSource = SignifyRemoteDataSource()

    @Provides
    fun getCartRemoteDataSource(): CartRemoteDataSource = CartItemRemoteDataSource()

    @Provides
    fun getCartItemCountRemoteDataSource(): ItemRemoteDataSource = CartItemCountRemoteDataSource()

    @Provides
    fun getLocalMediaDataSource(): LocalMediaDataSource = LocalMediaDataSourceImpl()
}
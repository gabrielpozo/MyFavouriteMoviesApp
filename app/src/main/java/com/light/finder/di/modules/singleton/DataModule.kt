package com.light.finder.di.modules.singleton

import com.light.domain.*
import com.light.repository.*
import com.light.source.local.LocalMediaDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.*
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun getApiRepository(
        remoteDataSource: RemoteDataSource,
        localMediaDataSource: LocalMediaDataSource,
        localPreferenceDataSource: LocalPreferenceDataSource,
        legendRemoteDataSource: RemoteFetchLegendDataSource

    ): CategoryRepository =
        CategoryRepositoryImpl(
            remoteDataSource,
            localPreferenceDataSource,
            localMediaDataSource,
            legendRemoteDataSource
        )

    @Provides
    fun getCartRepository(
        itemRemoteDataSource: CartRemoteDataSource
    ): CartRepository =
        CartRepositoryImpl(itemRemoteDataSource)

    @Provides
    fun getCartItemCountRepository(
        itemRemoteDataSource: ItemRemoteDataSource
    ): CartItemCountRepository =
        CartItemCountRepositoryImpl(itemRemoteDataSource)

    @Provides
    fun getLegendTagsRepository(
        legendRemoteDataSource: RemoteFetchLegendDataSource,
        localPreferenceDataSource: LocalPreferenceDataSource
    ): LegendRepository =
        LegendRepositoryImpl(legendRemoteDataSource, localPreferenceDataSource)

    @Provides
    fun getBrowsingProductsRepository(
        remoteFetchBrowsingSource: RemoteFetchBrowsingDataSource,
        localPreferenceDataSource: LocalPreferenceDataSource,
        remoteFetchLegendDataSource: RemoteFetchLegendDataSource
    ): BrowseLightBulbsRepository =
        BrowseLightBulbsRepositoryImpl(
            remoteFetchBrowsingSource,
            localPreferenceDataSource,
            remoteFetchLegendDataSource
        )

    @Provides
    fun getShapeBrowsingRepository(localPreferenceDataSource: LocalPreferenceDataSource): ShapeLightBulbRepository =
        ShapeLightBulbsRepositoryImpl(localPreferenceDataSource)
}
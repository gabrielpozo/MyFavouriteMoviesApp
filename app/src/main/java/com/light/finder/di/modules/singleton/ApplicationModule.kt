package com.light.finder.di.modules.singleton

import android.app.Application
import com.light.finder.data.source.local.LocalMediaDataSourceImpl
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.local.db.BrowseRoomDataBase
import com.light.finder.data.source.local.db.BrowseRoomDataSource
import com.light.finder.data.source.local.db.dao.BrowseDao
import com.light.finder.data.source.remote.*
import com.light.source.local.LocalMediaDataSource
import com.light.source.local.LocalPreferenceDataSource
import com.light.source.remote.*
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun getRemoteDataSource(): RemoteDataSource = SignifyRemoteDataSource()

    @Provides
    fun getCartRemoteDataSource(app: Application): CartRemoteDataSource =
        CartItemRemoteDataSource(app)

    @Provides
    fun getCartItemCountRemoteDataSource(app: Application): ItemRemoteDataSource =
        CartItemCountRemoteDataSource(app)

    @Provides
    fun getLegendDataSource(): RemoteFetchLegendDataSource =
        LegendRemoteDataSource()

    @Provides
    fun getBrowsingProductRemoteDataSource(): RemoteFetchBrowsingDataSource =
        BrowsingRemoteDataSource()

    @Provides
    fun getMoviesDao(app: Application): BrowseDao = BrowseRoomDataBase.getDatabase(app).browseDao()

    @Provides
    fun getLocalBrowsingDataBase(browseDao: BrowseDao): BrowseRoomDataSource =
        BrowseRoomDataSource(browseDao)

    @Provides
    fun getLocalMediaDataSource(): LocalMediaDataSource = LocalMediaDataSourceImpl()

    @Provides
    fun getLocalPreferenceDataSource(app: Application): LocalPreferenceDataSource =
        LocalPreferenceDataSourceImpl(app)

}
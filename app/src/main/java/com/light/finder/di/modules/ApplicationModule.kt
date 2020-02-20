package com.light.finder.di.modules

import com.light.finder.data.source.remote.SignifyRemoteDataSource
import com.light.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun getRemoteDataSource(): RemoteDataSource = SignifyRemoteDataSource()
}
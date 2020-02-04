package com.accenture.signify.di.modules

import com.accenture.signify.data.source.remote.SignifyRemoteDataSource
import com.accenture.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun getRemoteDataSource(): RemoteDataSource = SignifyRemoteDataSource()
}
package com.light.finder.di.modules

import com.light.domain.CategoryRepository
import com.light.repository.CategoryRepositoryImpl
import com.light.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun getApiRepository(
        remoteDataSource: RemoteDataSource
    ): CategoryRepository =
        CategoryRepositoryImpl(remoteDataSource)
}
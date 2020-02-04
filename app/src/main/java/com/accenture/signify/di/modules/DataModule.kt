package com.accenture.signify.di.modules

import com.accenture.domain.CategoryRepository
import com.accenture.repository.CategoryRepositoryImpl
import com.accenture.source.RemoteDataSource
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
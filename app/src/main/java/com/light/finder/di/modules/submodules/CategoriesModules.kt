package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.CategoryViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class CategoriesModule {

    @Provides
    fun categoryViewModel(
    ) = CategoryViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(CategoriesModule::class)])
interface CategoriesComponent {
    val categoryViewModel: CategoryViewModel
}
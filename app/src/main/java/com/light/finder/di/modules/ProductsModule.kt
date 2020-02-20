package com.light.finder.di.modules

import com.light.presentation.viewmodels.ProductsViewModel
import com.light.usecases.GetFilterButtonsUseCase
import com.light.usecases.GetProductsFilteredUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class ProductsModule {

    @Provides
    fun getProductsFilteredUseCase() =
        GetProductsFilteredUseCase()

    @Provides
    fun getFilterListUseCase() =
        GetFilterButtonsUseCase()

    @Provides
    fun categoryViewModel(
        productsFilteredUseCase: GetProductsFilteredUseCase,
        filterButtonsUseCase: GetFilterButtonsUseCase
    ) = ProductsViewModel(
        productsFilteredUseCase,
        filterButtonsUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(ProductsModule::class)])
interface ProductsComponent {
    val productsViewModel: ProductsViewModel
}


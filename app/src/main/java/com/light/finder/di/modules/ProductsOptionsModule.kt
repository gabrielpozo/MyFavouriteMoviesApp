package com.light.finder.di.modules

import com.light.presentation.viewmodels.ProductsOptionsViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class ProductsOptionsModule {

    @Provides
    fun productsOptionsViewModel(
    ) = ProductsOptionsViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(ProductsOptionsModule::class)])
interface ProductsOptionsComponent {
    val productsOptionsViewModel: ProductsOptionsViewModel
}
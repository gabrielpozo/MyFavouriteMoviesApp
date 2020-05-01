package com.light.finder.di.modules

import com.light.finder.CameraActivity
import com.light.finder.common.ScreenNavigator
import com.light.finder.di.CameraScope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class LightFinderModule(private val context: CameraActivity) {

    @CameraScope
    @Provides
    fun getScreenNavigator() =
        ScreenNavigator(context)
}

@CameraScope
@Subcomponent(modules = [(LightFinderModule::class)])
interface LightFinderComponent {
    val screenNavigator: ScreenNavigator

    fun plus(module: CategoriesModule): CategoriesComponent
    fun plus(module: CameraModule): CameraComponent
    fun plus(module: DetailModule): DetailComponent
    fun plus(module: CartModule): CartComponent
    fun plus(module: AboutModule) : AboutComponent
}
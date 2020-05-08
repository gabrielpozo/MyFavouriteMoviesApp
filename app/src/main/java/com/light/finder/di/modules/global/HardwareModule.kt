package com.light.finder.di.modules.global

import com.light.finder.common.ShakeDetector
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class HardwareModule(private val listener: ShakeDetector.Listener) {

    @GlobalHardwareScope
    @Provides
    fun getItemCountUseCase() =
        ShakeDetector(listener)
}


@GlobalHardwareScope
@Subcomponent(modules = [(HardwareModule::class)])
interface HardwareInstanceComponents {
    val shakeDetector: ShakeDetector
}

package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.LiveAmbianceViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class LiveAmbianceModule {
    @Provides
    fun liveAmbianceViewModel() = LiveAmbianceViewModel(
      Dispatchers.Main
    )
}

@Subcomponent(modules = [(LiveAmbianceModule::class)])
interface LiveAmbianceComponent {
    val liveAmbianceViewModel: LiveAmbianceViewModel
}
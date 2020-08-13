package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.light.usecases.GetLegendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getLegendUseCase: GetLegendUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()

    init {
        // TODO: Improve this by implementing Lottie listener
        launch {
            onRetrieveLegendTags()
            delay(4000)
            mutableLiveData.postValue(SplashState.CameraActivity)
        }
    }

    private fun onRetrieveLegendTags() {
        launch {
            getLegendUseCase.execute()
        }
    }
}


sealed class SplashState {
    object CameraActivity : SplashState()
}
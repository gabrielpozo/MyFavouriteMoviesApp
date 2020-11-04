package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.usecases.GetBearerTokenUseCase
import com.light.usecases.GetLegendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getLegendUseCase: GetLegendUseCase,
    private val getBearerTokenUseCase: GetBearerTokenUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()
    private var startTime = 0L
    private val minimumDelay = 1000L

    init {
        launch {
            startTime = System.currentTimeMillis()
            getBearerTokenUseCase.execute()
            getLegendUseCase.execute()
            navigateToApp()
        }
    }

    private fun navigateToApp() {
        val remainingTime = minimumDelay - (System.currentTimeMillis() - startTime)

        launch {
            delay(remainingTime)
            mutableLiveData.postValue(SplashState.CameraActivity)
        }
    }

}

sealed class SplashState {
    object CameraActivity : SplashState()
}

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
            retrieveBearerToken()
        }
    }

    private fun onRetrieveToken() {
        launch {
            getLegendUseCase.execute(onSuccess = ::onRetrieveLegend)
        }
    }

    private fun onRetrieveLegend() {
        val remainingTime = minimumDelay - (System.currentTimeMillis() - startTime)

        launch {
            delay(remainingTime)
            mutableLiveData.postValue(SplashState.CameraActivity)
        }
    }

    private fun retrieveBearerToken() {
        launch {
            getBearerTokenUseCase.execute(onSuccess = ::onRetrieveToken)
        }
    }
}


sealed class SplashState {
    object CameraActivity : SplashState()
}

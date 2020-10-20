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

    init {
        launch {
            onRetrieveBearerToken()
            delay(4000)
            mutableLiveData.postValue(SplashState.CameraActivity)
        }
    }

    private fun onRetrieveLegendTags() {
        launch {
            getLegendUseCase.execute()
        }
    }

    private fun onRetrieveBearerToken() {
        launch {
            getBearerTokenUseCase.execute(onSuccess = ::onRetrieveLegendTags)
        }
    }
}

sealed class SplashState {
    object CameraActivity : SplashState()
}
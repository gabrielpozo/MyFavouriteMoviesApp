package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()
    init {
        // TODO: Improve this by implementing Lottie listener
        GlobalScope.launch {
            delay(4000)
            mutableLiveData.postValue(SplashState.CameraActivity)
        }
    }
}
sealed class SplashState {
    object CameraActivity : SplashState()
}
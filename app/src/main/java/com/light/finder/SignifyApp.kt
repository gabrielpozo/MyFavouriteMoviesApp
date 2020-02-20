package com.light.finder

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.light.finder.di.ApplicationComponent
import com.light.finder.di.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import timber.log.Timber


class SignifyApp : Application(), CameraXConfig.Provider {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }

        applicationComponent = DaggerApplicationComponent.factory().create(this)

    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}
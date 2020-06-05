package com.light.finder

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.light.finder.common.InternetUtil
import com.light.finder.di.ApplicationComponent
import com.light.finder.di.DaggerApplicationComponent
import com.usabilla.sdk.ubform.Usabilla
import io.fabric.sdk.android.Fabric
import timber.log.Timber


class SignifyApp : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            //debug
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
            enableCrashlyticsDebugMode()
            Usabilla.debugEnabled = true
        }

        InternetUtil.init(this)
        Usabilla.initialize(this)

        applicationComponent = DaggerApplicationComponent.factory().create(this)


    }


    private fun enableCrashlyticsDebugMode() {
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true)
            .build()
        Fabric.with(fabric)
    }
}
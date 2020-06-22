package com.light.finder

import android.app.Application
import com.facebook.stetho.Stetho
import com.light.finder.common.InternetUtil
import com.light.finder.di.ApplicationComponent
import com.light.finder.di.DaggerApplicationComponent
import com.usabilla.sdk.ubform.Usabilla
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
        }

        InternetUtil.init(this)

        applicationComponent = DaggerApplicationComponent.factory().create(this)


    }
}
package com.light.finder

import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.stetho.Stetho
import com.light.finder.common.InternetUtil
import com.light.finder.di.ApplicationComponent
import com.light.finder.di.DaggerApplicationComponent
import timber.log.Timber


class SignifyApp : Application() {

    companion object {
        lateinit var instance: SignifyApp
        fun getContext(): Context? {
            return instance.applicationContext
        }
    }

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        if (BuildConfig.DEBUG) {
            //debug
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }

        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        InternetUtil.init(this)

        applicationComponent = DaggerApplicationComponent.factory().create(this)


    }
}
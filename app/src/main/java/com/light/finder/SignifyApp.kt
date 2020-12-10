package com.light.finder

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.light.finder.common.InternetUtil
import com.light.finder.di.ApplicationComponent
import com.light.finder.di.DaggerApplicationComponent
import com.light.util.QA
import com.usabilla.sdk.ubform.Usabilla
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

        InternetUtil.init(this)

        applicationComponent = DaggerApplicationComponent.factory().create(this)
        Usabilla.initialize(
            this, if (BuildConfig.FLAVOR == QA) {
                UsabillaActivity.APP_ID_QA
            } else UsabillaActivity.APP_ID_PROD, null, null
        )
    }
}
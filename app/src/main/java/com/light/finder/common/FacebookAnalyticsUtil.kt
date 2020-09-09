package com.light.finder.common

import android.content.Context
import android.os.Bundle
import com.facebook.appevents.AppEventsLogger
import com.light.finder.extensions.bundleFor


class FacebookAnalyticsUtil private constructor(context: Context) {
    private var isConsentAccepted = false
    private var logger: AppEventsLogger? = null

    init {
        logger = AppEventsLogger.newLogger(context)
        isConsentAccepted = PrefManager(context).isConsentAccepted

    }

    fun logEventOnFacebookSdk(typeEvent: String, body: Bundle.() -> Unit) {
        if (isConsentAccepted)
        logger?.logEvent(typeEvent, bundleFor(body))
    }

    fun setConsent(checked: Boolean) {
        isConsentAccepted = checked
    }

    companion object : SingletonHolder<FacebookAnalyticsUtil, Context>(::FacebookAnalyticsUtil)

}


open class SingletonHolder<out T, in A>(private val constructor: (A) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        return when {
            instance != null -> instance!!
            else -> synchronized(this) {
                if (instance == null) instance = constructor(arg)
                instance!!
            }
        }
    }
}
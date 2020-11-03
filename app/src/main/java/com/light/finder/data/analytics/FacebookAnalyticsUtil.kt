package com.light.finder.data.analytics

import android.content.Context
import android.os.Bundle
import com.facebook.appevents.AppEventsLogger
import com.light.finder.common.PrefManager
import com.light.finder.extensions.bundleFor
import com.light.util.SingletonHolder


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

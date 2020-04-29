package com.light.finder.extensions


import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


inline fun bundleFor(body: Bundle.() -> Unit): Bundle =
    Bundle().apply(body)


inline fun FirebaseAnalytics.logEventOnGoogleTagManager(typeEvent: String, body: Bundle.() -> Unit) {
    logEvent(typeEvent, bundleFor(body))

}


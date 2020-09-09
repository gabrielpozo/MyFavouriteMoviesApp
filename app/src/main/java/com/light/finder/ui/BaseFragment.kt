package com.light.finder.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.CameraLightFinderActivity
import com.light.finder.common.FacebookAnalyticsUtil
import com.light.finder.navigators.ScreenNavigator
import com.light.finder.di.modules.camera.LightFinderComponent


abstract class BaseFragment : Fragment() {
    protected lateinit var screenNavigator: ScreenNavigator
    protected lateinit var lightFinderComponent: LightFinderComponent
    protected lateinit var firebaseAnalytics: FirebaseAnalytics
    protected lateinit var facebookAnalyticsUtil: FacebookAnalyticsUtil

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CameraLightFinderActivity) {
            lightFinderComponent = context.lightFinderComponent
            screenNavigator = lightFinderComponent.screenNavigator
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            facebookAnalyticsUtil = FacebookAnalyticsUtil.getInstance(context)
        }
    }
}
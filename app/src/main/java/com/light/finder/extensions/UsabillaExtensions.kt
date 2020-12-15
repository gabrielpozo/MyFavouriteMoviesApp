package com.light.finder.extensions

import androidx.fragment.app.FragmentActivity
import com.light.finder.R
import com.usabilla.sdk.ubform.Usabilla

fun FragmentActivity.sendUsabillaCampaignEvent() {
    Usabilla.updateFragmentManager(supportFragmentManager)
    Usabilla.sendEvent(this, this.getString(R.string.payment_successful))
}

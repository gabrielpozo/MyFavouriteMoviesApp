package com.light.finder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.usabilla.sdk.ubform.UbConstants.INTENT_CLOSE_FORM
import com.usabilla.sdk.ubform.Usabilla
import com.usabilla.sdk.ubform.UsabillaFormCallback
import com.usabilla.sdk.ubform.UsabillaReadyCallback
import com.usabilla.sdk.ubform.sdk.form.FormClient


class UsabillaActivity : AppCompatActivity(), UsabillaFormCallback, UsabillaReadyCallback {

    private val closerFilter: IntentFilter = IntentFilter(INTENT_CLOSE_FORM)
    private val fragmentTag = "UsabillaFragment"
    private var formClient: FormClient? = null
    private val usabilla: Usabilla = Usabilla


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usabilla)
        initializeSdk()
    }

    private fun attachFragment() {
        if (!isFinishing) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.usabila_container, formClient!!.fragment, fragmentTag).commit()
        }
    }


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(usabillaReceiverClosePassive, closerFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverClosePassive)
    }

    private val usabillaReceiverClosePassive: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (formClient != null) {
                supportFragmentManager.beginTransaction().remove(formClient!!.fragment).commit()
                finish()
            }
        }
    }



    override fun formLoadSuccess(form: FormClient) {
        formClient = form
        attachFragment()
    }

    private fun initializeSdk() {
        // In the initialize method the third parameter defines a custom http client that can replace
        // the default one used by the SDK (Volley).
        // If `null` is passed then the default client will be used.
        Usabilla.initialize(this, APP_ID, null, this)
    }

    override fun mainButtonTextUpdated(text: String?) {
        // Use this text for your own navigation button.
        // Usually returns "Next" or "Submit".
    }

    override fun formLoadFail() {
    }

    companion object {
        const val FORM_ID = "5eaa82dcd274636ddf6bc8ce"
        const val APP_ID = "31e1d288-ee4b-4a5c-a0bf-d044ba1de901"
        var isSentEvent: Boolean = false

        fun sendEvent(context: Context, fragmentManager: FragmentManager) {
            if (isSentEvent) {
                Usabilla.resetCampaignData(context)
                Usabilla.updateFragmentManager(fragmentManager)
                Usabilla.sendEvent(context, context.getString(R.string.payment_successful))
            }
        }
    }

    override fun onUsabillaInitialized() {
        // This callback will be called once the initialization process of the SDK finishes.
        // In case an appId was provided during initialization, then the SDK starts to be able
        // to process events right after this callback is called.
        Usabilla.debugEnabled = true
        Usabilla.updateFragmentManager(supportFragmentManager)
        Usabilla.preloadFeedbackForms(listOf(FORM_ID)) // make sure that preloadFeedbackForms is called only when online
        //usabilla.removeCachedForms() // use that
        Usabilla.loadFeedbackForm(FORM_ID, null, null, this)
    }

// TODO: Investigate Completion Callback for Analytics.
//    private val usabillaReceiverClosePassive: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            // The passive feedback form needs to be closed and the feedback result is returned
//            val res: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT)
//        }
//    }
//
//    private val usabillaReceiverCloseCampaign: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            // The campaign feedback form has been closed and the feedback result is returned
//            val res: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverClosePassive, IntentFilter(UbConstants.INTENT_CLOSE_FORM))
//        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverCloseCampaign, IntentFilter(UbConstants.INTENT_CLOSE_CAMPAIGN))
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverClosePassive)
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverCloseCampaign)
//    }
}

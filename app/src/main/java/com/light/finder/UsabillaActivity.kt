package com.light.finder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.light.util.QA
import com.usabilla.sdk.ubform.UbConstants.INTENT_CLOSE_FORM
import com.usabilla.sdk.ubform.Usabilla
import com.usabilla.sdk.ubform.UsabillaFormCallback
import com.usabilla.sdk.ubform.sdk.form.FormClient


class UsabillaActivity : AppCompatActivity(), UsabillaFormCallback {

    private val closerFilter: IntentFilter = IntentFilter(INTENT_CLOSE_FORM)
    private val fragmentTag = "UsabillaFragment"
    private var formClient: FormClient? = null
    private val usabilla: Usabilla = Usabilla


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usabilla)
        setLoadForm()
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

    private fun setLoadForm() {
        // In the initialize method the third parameter defines a custom http client that can replace
        // the default one used by the SDK (Volley).
        // If `null` is passed then the default client will be used.
        usabilla.debugEnabled = (BuildConfig.FLAVOR == QA)
        usabilla.updateFragmentManager(supportFragmentManager)
        usabilla.loadFeedbackForm(FORM_ID, null, null, this)
    }

    override fun mainButtonTextUpdated(text: String?) {
        // Use this text for your own navigation button.
        // Usually returns "Next" or "Submit".
    }

    override fun formLoadFail() {}

    companion object {
        const val FORM_ID = "5eaa82dcd274636ddf6bc8ce"
        const val APP_ID_PROD = "31e1d288-ee4b-4a5c-a0bf-d044ba1de901"
        const val APP_ID_QA = "78950726-0ccb-4bd6-b4ce-52f3007b5d20"
    }
}

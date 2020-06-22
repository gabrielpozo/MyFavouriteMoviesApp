package com.light.finder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.usabilla.sdk.ubform.UbConstants
import com.usabilla.sdk.ubform.Usabilla
import com.usabilla.sdk.ubform.UsabillaFormCallback
import com.usabilla.sdk.ubform.sdk.form.FormClient


class UsabillaActivity : AppCompatActivity(), UsabillaFormCallback {

    var usabillaCloserPassive: BroadcastReceiver? = null
    val closerFilterPassive: IntentFilter = IntentFilter(UbConstants.INTENT_CLOSE_FORM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usabilla)
        Usabilla.loadFeedbackForm(FORM_ID, null, null, this)
    }


    override fun onStart() {
        super.onStart()
        setupCloserBroadcastReceiver()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(usabillaCloserPassive!!, closerFilterPassive)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaCloserPassive!!)
    }

    private fun setupCloserBroadcastReceiver() {
        usabillaCloserPassive = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                finish()
            }
        }
    }


    override fun formLoadSuccess(form: FormClient?) {
        if (form?.fragment == null) {
            return
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.usabila_container, form.fragment, "FRAGMENT_TAG")
            .commit()
    }

    override fun mainButtonTextUpdated(text: String?) {
        if (text == "Cancel") {
            Usabilla.dismiss(applicationContext)
            finish()
        }
    }

    override fun formLoadFail() {
    }

    companion object {
        const val FORM_ID = "5eaa82dcd274636ddf6bc8ce"
    }
}

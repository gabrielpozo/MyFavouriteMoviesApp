package com.light.finder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.usabilla.sdk.ubform.Usabilla
import com.usabilla.sdk.ubform.UsabillaFormCallback
import com.usabilla.sdk.ubform.sdk.form.FormClient

class UsabillaActivity : AppCompatActivity(), UsabillaFormCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usabilla)
        Usabilla.loadFeedbackForm("5eaa82dcd274636ddf6bc8ce", null, null, this)

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
    }

    override fun formLoadFail() {
    }
}

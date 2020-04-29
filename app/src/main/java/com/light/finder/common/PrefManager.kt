package com.light.finder.common

import android.content.Context
import android.content.SharedPreferences


class PrefManager(_context: Context) {

    companion object {
        private const val PREF_NAME = "sharedPref"

        private const val IS_TERMS_ACCEPTED = "isTermsAccepted"
        private const val IS_CONSENT_ACCEPTED = "isConsentAccepted"
    }
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    private var PRIVATE_MODE = 0

    var isTermsAccepted: Boolean
        get() = pref.getBoolean(IS_TERMS_ACCEPTED, false)
        set(isAccepted) {
            editor.putBoolean(IS_TERMS_ACCEPTED, isAccepted)
            editor.commit()
        }

    var isConsentAccepted: Boolean
        get() = pref.getBoolean(IS_CONSENT_ACCEPTED, false)
        set(isAccepted) {
            editor.putBoolean(IS_CONSENT_ACCEPTED, isAccepted)
            editor.commit()
        }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit().also { it.apply() }
    }
}


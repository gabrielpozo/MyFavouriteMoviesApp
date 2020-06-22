package com.light.finder.data.source.remote.reports

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.light.util.FAILED_RECOGNITION
import com.light.util.FORMAT_INVALID
import com.light.util.SERVER_ERROR_RECOGNITION
import com.light.util.TIMEOUT_ERROR_RECOGNITION
import timber.log.Timber

class CrashlyticsException(
    private val code: Int,
    private val field: String?,
    private val value: Int?
) {

    private var msg: String = "Crashlytics Exception"

    init {
        when (code) {
            204 -> msg = FAILED_RECOGNITION
            422 -> msg = FORMAT_INVALID
            500 -> msg = SERVER_ERROR_RECOGNITION
            408 -> msg = TIMEOUT_ERROR_RECOGNITION
        }
    }

    fun logException() {
        val e = Exception(code.toString())
        e.stackTrace = arrayOfNulls(0)

        if (field != null) {
            FirebaseCrashlytics.getInstance().setCustomKey("Field", field)
        }

        if (value != null) {
            FirebaseCrashlytics.getInstance().setCustomKey("Value", value)
        }

        FirebaseCrashlytics.getInstance().setCustomKey("Response Code", code)
        FirebaseCrashlytics.getInstance().setCustomKey("Reason", msg)
        FirebaseCrashlytics.getInstance().log(msg)
        FirebaseCrashlytics.getInstance().recordException(e)
        Timber.d(e)
    }

}
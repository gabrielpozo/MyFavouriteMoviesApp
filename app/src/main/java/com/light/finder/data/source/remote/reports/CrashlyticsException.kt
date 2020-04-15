package com.light.finder.data.source.remote.reports

import com.crashlytics.android.Crashlytics
import com.light.util.FAILED_RECOGNITION
import com.light.util.FORMAT_INVALID
import com.light.util.SERVER_ERROR_RECOGNITION
import com.light.util.TIMEOUT_ERROR_RECOGNITION

class CrashlyticsException(var tag: Int) : Exception(tag.toString()) {

    private var msg: String? = "Crashlytics Exception"

    init {
        when (tag) {
            204 -> msg = FAILED_RECOGNITION
            422 -> msg = FORMAT_INVALID
            500 -> msg = SERVER_ERROR_RECOGNITION
            408 -> msg = TIMEOUT_ERROR_RECOGNITION
        }

        //val e = Exception(msg)
        Crashlytics.setInt("Response Code", tag)
        Crashlytics.log(msg)
    }

}
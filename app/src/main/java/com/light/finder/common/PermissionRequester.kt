package com.light.finder.common

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener


class PermissionRequester(private val activity: Activity, private val permission: String) {
    fun request(continuation: (Boolean) -> Unit, isPermanentlyDenied: (Boolean) -> Unit = {}) {
        Dexter
            .withContext(activity)
            .withPermission(permission)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    continuation(true)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    continuation(false)
                    isPermanentlyDenied(response?.isPermanentlyDenied ?: false)

                }

            }
            ).check()
    }

}
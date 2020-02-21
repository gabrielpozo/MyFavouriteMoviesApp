package com.light.finder.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.light.finder.extensions.isConnectedToNetwork

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (connectivityReceiverListener != null) {
            context?.isConnectedToNetwork()?.let {
                connectivityReceiverListener?.onNetworkConnectionChanged(
                    it
                )
            }
        }

    }
    
    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}
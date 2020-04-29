package com.light.finder.common

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

object InternetUtil : LiveData<Boolean>() {
    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var application: Application
    private var lastIsInternetOn: Boolean? = null

    fun init(application: Application) {
        this.application = application
        this.lastIsInternetOn = isInternetOn()
    }

    fun isInternetOn(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    override fun onActive() {
        registerBroadCastReceiver()
    }

    override fun onInactive() {
        unRegisterBroadCastReceiver()
    }

    private fun registerBroadCastReceiver() {
        if (broadcastReceiver == null) {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(_context: Context, intent: Intent) {

                    if (lastIsInternetOn != isInternetOn()) {
                        val extras = intent.extras
                        val info = extras?.getParcelable<NetworkInfo>("networkInfo")
                        value = info?.state == NetworkInfo.State.CONNECTED
                    }

                    lastIsInternetOn = isInternetOn()
                }
            }

            application.registerReceiver(broadcastReceiver, filter)
        }
    }

    private fun unRegisterBroadCastReceiver() {
        if (broadcastReceiver != null) {
            application.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }
}
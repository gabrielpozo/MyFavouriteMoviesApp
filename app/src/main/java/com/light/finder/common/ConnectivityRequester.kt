package com.light.finder.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectivityRequester(private val context: Context) {

    fun checkConnection(continuation: (Boolean) -> Unit) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork


        continuation.invoke(isConnected(connectivityManager.getNetworkCapabilities(network)))
    }


    private fun isConnected(connection: NetworkCapabilities?): Boolean =
        connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))


}
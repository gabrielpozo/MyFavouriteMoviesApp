package com.light.source.local


interface LocalSharedPrefDataSource {
    fun addToSharedPref(isAccepted: Boolean): Boolean
}
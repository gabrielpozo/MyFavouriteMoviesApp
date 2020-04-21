package com.light.finder.common


interface ReloadingCallback {
    fun setCurrentlyReloaded(reloadCart: Boolean)
    fun hasBeenReload(): Boolean
}
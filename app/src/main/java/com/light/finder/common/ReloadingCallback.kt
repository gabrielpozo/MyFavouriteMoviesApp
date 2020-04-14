package com.light.finder.common



interface ReloadingCallback {
    fun setReload(reloadCart: Boolean)
    fun getReload():Boolean
}
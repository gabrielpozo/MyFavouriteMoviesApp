package com.light.finder.common


interface ActivityCallback {
    fun setBottomBarInvisibility(invisible: Boolean)
    fun onBadgeCountChanged(badgeCount: Int)
    fun onCartCleared()
    fun onBottomBarBlocked(isClickable: Boolean)
    fun onInternetConnectionLost()
}
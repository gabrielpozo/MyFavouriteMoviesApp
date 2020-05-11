package com.light.finder.common


interface ActivityCallback {
    fun onVisibilityChanged(invisible: Boolean)
    fun onBadgeCountChanged(badgeCount: Int)
    fun onCartCleared()
    fun onBottomBarBlocked(isClickable: Boolean)
    fun onInternetConnectionLost()
}
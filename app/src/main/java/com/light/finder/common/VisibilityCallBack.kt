package com.light.finder.common

interface VisibilityCallBack{
    fun onVisibilityChanged(invisible: Boolean)
    fun onBadgeCountChanged(badgeCount: Int)
    fun onCartCleared()
    fun onBottomBarBlocked(isClickable: Boolean)
}
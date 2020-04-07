package com.light.finder.common

import com.light.domain.model.Product

interface VisibilityCallBack{
    fun onVisibilityChanged(invisible: Boolean)
    fun onBadgeCountChanged(badgeCount: Int)
    fun onCartCleared()
    fun onBottomBarBlocked(isClickable: Boolean)
    fun navigateToVariationActivity(productList: List<Product>)
}
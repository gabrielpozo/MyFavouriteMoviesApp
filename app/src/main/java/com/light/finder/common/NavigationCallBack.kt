package com.light.finder.common

import com.light.domain.model.Product

interface NavigationCallBack {
    fun navigateToVariationActivity(productList: List<Product>)
    fun navigateToTipsAndTricksActivity()
}
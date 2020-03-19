package com.light.domain.model


data class FilterWattage(val nameFilter: String, val type: TYPE, var isActive: Boolean = false)
data class FilterColor(val nameFilter: String, val type: TYPE, var isActive: Boolean = false)
data class FilterFinish(val nameFilter: String, val type: TYPE, var isActive: Boolean = false)


enum class TYPE {
    SPEC1, SPEC3, PRODUCT_SCENE
}
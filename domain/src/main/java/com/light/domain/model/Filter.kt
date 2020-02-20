package com.light.domain.model


data class Filter(val nameFilter: String, val type: TYPE, var isActive: Boolean = false)

enum class TYPE {
    SPEC1, SPEC3, PRODUCT_SCENE
}
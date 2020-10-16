package com.light.finder.extensions

fun Int.pxToDp(density: Float): Int = this / density.toInt()
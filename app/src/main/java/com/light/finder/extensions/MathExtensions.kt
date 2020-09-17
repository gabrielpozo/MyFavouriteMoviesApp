package com.light.finder.extensions

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()
fun Int.pxToDp(density: Float): Int = this / density.toInt()
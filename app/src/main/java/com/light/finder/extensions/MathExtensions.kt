package com.light.finder.extensions

import java.text.DecimalFormatSymbols

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()
fun Float.toDoubleEx() : Double {
    val decimalSymbol = DecimalFormatSymbols.getInstance().decimalSeparator
    return if (decimalSymbol == ',') {
        this.toString().replace(decimalSymbol, '.').toDouble()
    } else {
        this.toDouble()
    }
}
fun Int.pxToDp(density: Float): Int = this / density.toInt()
package com.light.finder.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.light.finder.R
import com.light.finder.common.SafeClickListener
import java.io.File
import java.util.*


private const val bitmapWidth = 1650
private const val bitmapHeight = 2200


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.loadFile(file: File) {
    Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(this)
}

fun ImageView.loadImage(bitmap: Bitmap) {
    val matrix = Matrix()
    matrix.postRotate(90f)
    val scaledBitmap =
        Bitmap.createScaledBitmap(bitmap, bitmapHeight, bitmapWidth, true)
    val rotatedBitmap = Bitmap.createBitmap(
        scaledBitmap,
        0,
        0,
        scaledBitmap.width,
        scaledBitmap.height,
        matrix,
        true
    )

    this.setImageBitmap(rotatedBitmap)
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


fun String.getIntFormatter(number: Number): String =
    Formatter().format(this, number).toString()

fun String.getStringFormatter(chain: String): String = String.format(this, chain)

fun TextView.endDrawable(@DrawableRes id: Int = 0) {
    val drawable = ContextCompat.getDrawable(context, id)
    this.setCompoundDrawables(null, null, drawable, null)
}

fun TextView.endDrawableIcon(@DrawableRes id: Int = 0) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView.getColorString(color: String): Int = when (color) {
    context.getString(R.string.warm) -> {
        R.drawable.ic_warm
    }
    context.getString(R.string.white_warm) -> {
        R.drawable.ic_warm_white
    }
    context.getString(R.string.cool_white) -> {
        R.drawable.ic_cool_white
    }
    context.getString(R.string.daylight) -> {
        R.drawable.ic_daylight
    }

    else -> R.drawable.ic_warm
}









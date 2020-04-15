package com.light.finder.extensions

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.light.finder.R
import com.light.finder.common.SafeClickListener
import kotlinx.android.synthetic.main.item_card_filter_unselected.view.*
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

fun View.slideVertically(distance: Float, duration: Long = 1000, hide: Boolean = false) {
    val view = this
    this.animate().translationY(distance).setDuration(500)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                if (hide) {
                    view.gone()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                view.visible()
            }
        })
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

fun String?.getSplitUrl(): String {
    var chain = "/"
    val splitedList = this?.split("/") ?: return ""
    for (index in (splitedList.size - 4) until splitedList.size - 1) {
        chain += splitedList[index] + "/"
    }

    return chain
}


fun TextView.endDrawableIcon(@DrawableRes id: Int = 0) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, id, 0)
}












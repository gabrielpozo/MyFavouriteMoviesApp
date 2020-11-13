package com.light.finder.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.light.finder.common.SafeClickListener
import timber.log.Timber
import java.io.File
import java.util.*


private const val bitmapWidth = 1650
private const val bitmapHeight = 2200
private var defaultInterval: Int = 1000


fun TextView.setHtmlText(source: String) {
    this.text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

internal fun Drawable.tint(@ColorInt color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hideWithAnimation() {
    val view = this
    view.animate()
        .alpha(0f)
        .setDuration(500L)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
            }
        })
}

fun View.showWithAnimation() {
    val view = this
    view.animate()
        .alpha(1f)
        .setDuration(500L)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })
}

fun View.slideVertically(distance: Float, duration: Long = 500, hide: Boolean = false) {
    val view = this
    this.animate().translationY(distance).setDuration(duration)
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

fun View.fadeOut(duration: Long = 200L, hide: Boolean = true) {
    val view = this
    this.animate()
        .alpha(0F)
        .setDuration(duration)
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
            }

        })
}


fun View.fadeIn(duration: Long = 500L) {
    val view = this
    view.visible()
    view.alpha = 0F
    this.animate()
        .setStartDelay(500L)
        .alpha(1F)
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {

            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
}

fun ImageView.loadFile(file: File) {
    Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).into(this)
}

fun ImageView.loadImage(bitmap: Bitmap, rotationDegree: Int) {
    Timber.d("RITIKA rotation degree: $rotationDegree  float:${rotationDegree.toFloat()}")
    val matrix = Matrix()
    matrix.postRotate(rotationDegree.toFloat())
    val scaledBitmap =
        Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
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

fun View.setSafeOnClickListener(
    onViewResponse: (Boolean) -> Unit = {},
    onSafeClick: (View) -> Unit
) {
    val safeClickListener = SafeClickListener {
        onViewResponse.invoke(true)
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


fun String.getIntFormatter(number: Number): String =
    Formatter().format(this, number).toString()

fun String.getStringFormatter(chain: String): String = String.format(this, chain)

fun String?.getSplitUrl(): String {
    if (this != "about:blank") {
        var chain = "/"
        val splitedList = this?.split("/") ?: return ""
        for (index in (splitedList.size - 3) until splitedList.size - 1) {
            chain += splitedList[index] + "/"
        }

        return chain
    } else {
        return this
    }
}

fun SpannableString.withClickableSpan(
    clickablePart: String,
    color: Int,
    onClickListener: () -> Unit
): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        private var lastTimeClicked: Long = 0

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                return
            }
            lastTimeClicked = SystemClock.elapsedRealtime()
            onClickListener.invoke()
        }

        override fun updateDrawState(text: TextPaint) {
            text.isUnderlineText = false
            text.color = color
            text.typeface = Typeface.DEFAULT_BOLD
        }
    }
    val clickablePartStart = indexOf(clickablePart)
    setSpan(
        clickableSpan,
        clickablePartStart,
        clickablePartStart + clickablePart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

fun Int.pluralOrSingular() =
    if (this > 1) {
        "s"
    } else {
        ""
    }













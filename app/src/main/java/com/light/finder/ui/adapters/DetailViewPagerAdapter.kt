package com.light.finder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.light.finder.R
import com.light.finder.extensions.loadUrlCenterCrop
import com.light.finder.extensions.setPlaceholder
import kotlinx.android.synthetic.main.slider_image_bulb.view.*

class DetailImageAdapter(private val context: Context, private val images: List<String>) :
    PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //var imageView: ImageView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_image_bulb, container, false)

        when {
            images[position].isBlank() -> {
                view.bulbPoster.setPlaceholder()
            }
            else -> {
                view.bulbPoster.loadUrlCenterCrop(images[position])
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

}

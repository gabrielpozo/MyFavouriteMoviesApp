package com.light.finder.ui.adapters

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.light.finder.R
import com.light.finder.extensions.loadUrl
import kotlinx.android.synthetic.main.slider_image_bulb.view.*

class DetailViewPagerAdapter internal constructor(
    context: Context,
    private val images: List<String> = emptyList()
) : RecyclerView.Adapter<DetailViewPagerAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_detail_view_pager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        //todo set image
        //holder.imageView.loadUrl()
    }

    override fun getItemCount() = images.size
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageViewDetail)
    }
}


class DetailImageAdapter(private val context: Context, private val images: List<String>) :
    PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //var imageView: ImageView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_image_bulb, container, false)

        view.bulbPoster.loadUrl(images[position])

        container.addView(view)
        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeAllViews()
    }

}

package com.light.finder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.light.finder.R

class DetailViewPagerAdapter internal constructor(
    context: Context,
    private val images: List<String>
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
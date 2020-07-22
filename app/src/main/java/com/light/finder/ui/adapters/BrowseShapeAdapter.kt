package com.light.finder.ui.adapters

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.light.domain.model.ProductBrowsing
import com.light.finder.R
import com.light.finder.extensions.inflate
import kotlinx.android.synthetic.main.item_browse_fitting.view.*

class BrowseShapeAdapter(
    private val listener: (ProductBrowsing) -> Unit,
    private val productsList: List<ProductBrowsing> = emptyList()
) :
    RecyclerView.Adapter<BrowseShapeAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var tracker: SelectionTracker<Long>? = null

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_browse_shape, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productsList[position]

        tracker?.let {
            holder.bind(product, it.isSelected(position.toLong()))
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

        fun bind(product: ProductBrowsing, isActivated: Boolean = false) {
            itemView.isActivated = isActivated

        }
    }
}

class BrowseShapeDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as BrowseShapeAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}


package com.accenture.signify.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.accenture.domain.model.Product
import com.accenture.signify.R
import com.accenture.signify.extensions.basicDiffUtil
import com.accenture.signify.extensions.inflate
import com.accenture.signify.extensions.loadUrl
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    var products: List<Product> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.productName == new.productName }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_product, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = products[position]
        holder.bind(category)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            itemView.product_name.text = product.productName
            itemView.product_description.text = product.productDescription
            itemView.productCover.loadUrl(product.productImage[0])
        }
    }
}
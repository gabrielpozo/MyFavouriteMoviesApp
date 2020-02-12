package com.accenture.signify.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.accenture.signify.R

class FilterResultAdapter : RecyclerView.Adapter<FilterResultAdapter.ViewHolder>(), Filterable {


    //todo change Any type
    val resultsFinalList = ArrayList<Any>()
    val resultsFilteredList = ArrayList<Any>()
    lateinit var context: Context

    fun setData(myList: ArrayList<Any>) {
        resultsFinalList.addAll(myList)
        resultsFilteredList.addAll(myList)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(holder: FilterResultAdapter.ViewHolder, position: Int) {


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterResultAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return resultsFinalList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(p0: CharSequence?): FilterResults {
                //todo filterString is the string we receive from the FilterAdapter
                val filterString = p0.toString().toLowerCase()
                val filteredList = ArrayList<Any>()
                for (item in resultsFilteredList) {
                    if (item.title.contains(filterString)) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults

            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                val result = DiffUtil.calculateDiff(
                    //todo do this with extension
                    FilterResultDiffCallBack(
                        p1?.values as ArrayList<Any>,
                        resultsFinalList
                    )
                )
                result.dispatchUpdatesTo(this@FilterResultAdapter)
                resultsFinalList.clear()
                resultsFinalList.addAll(p1?.values as ArrayList<Any>)
            }


        }
    }


}
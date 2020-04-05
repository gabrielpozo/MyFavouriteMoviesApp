package com.light.finder.ui.adapters

//todo add this to where you initialize the adapter
/*

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.list = filterList
        adapter.notifyDataSetChanged()

        tracker = SelectionTracker.Builder<Long>(
            "selectedFilter",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            FilterItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker
 */

//todo use tracker to observe the selected filter and filter the other recyclerview accordingly
/*
tracker?.addObserver(
    object : SelectionTracker.SelectionObserver<Long>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            val items = tracker?.selection!!

                //filter other recyclerview

        }
    })
 */

/*
class FilterItemDetailsLookUp(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as FilterAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}*/

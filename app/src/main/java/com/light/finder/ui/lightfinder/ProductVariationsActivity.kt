package com.light.finder.ui.lightfinder


import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.light.domain.model.FilterVariationCF
import com.light.finder.R
import com.light.finder.data.source.remote.ProductParcelable
import com.light.finder.di.modules.ProductsOptionsComponent
import com.light.finder.di.modules.ProductsOptionsModule
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.FilterColorAdapter
import com.light.finder.ui.adapters.FilterFinishAdapter
import com.light.finder.ui.adapters.FilterWattageAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.ProductsOptionsViewModel
import com.light.presentation.viewmodels.ProductsOptionsViewModel.*
import kotlinx.android.synthetic.main.layout_filter_dialog.*


class ProductVariationsActivity : AppCompatActivity() {

    companion object {
        const val PRODUCTS_OPTIONS_ID_KEY = "ProductsOptionsFragment::id"
        const val PRODUCT_LIST_EXTRA = "productListId"
        const val REQUEST_CODE_PRODUCT = 1
    }

    private lateinit var component: ProductsOptionsComponent
    private val viewModel: ProductsOptionsViewModel by lazy { getViewModel { component.productsOptionsViewModel } }
    private lateinit var filterWattageAdapter: FilterWattageAdapter
    private lateinit var filterColorAdapter: FilterColorAdapter
    private lateinit var filterFinishAdapter: FilterFinishAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        setContentView(R.layout.layout_filter_dialog)

        component = app.applicationComponent.plus(ProductsOptionsModule())

        intent.getParcelableArrayListExtra<ProductParcelable>(PRODUCTS_OPTIONS_ID_KEY)
            ?.let { productList ->
                viewModel.onRetrieveProductsVariation(
                    productList.deparcelizeProductList()
                )
            }

        initAdapters()
        setVariationsObservers()
        navigationObserver()
        setDoneClickListener()

    }


    private fun setVariationsObservers() {
        viewModel.dataFilterWattageButtons.observe(
            this,
            Observer(::observeFilteringWattage)
        )

        viewModel.dataFilterColorButtons.observe(
            this,
            Observer(::observeFilteringColor)
        )

        viewModel.dataFilterFinishButtons.observe(
            this,
            Observer(::observeFinishWattage)
        )

        viewModel.productSelected.observe(
            this,
            Observer(::observeProductSelectedResult)
        )
    }

    private fun navigationObserver() {
        viewModel.modelNavigation.observe(
            this,
            Observer(::navigateBackToDetail)
        )
    }

    private fun setDoneClickListener() {
        textViewDone.setOnClickListener {
            viewModel.onDoneButtonClicked()
        }
    }

    private fun initAdapters() {
        filterWattageAdapter = FilterWattageAdapter(::handleFilterWattagePressed)
        recyclerViewWattage.adapter = filterWattageAdapter

        filterColorAdapter = FilterColorAdapter(::handleFilterColorPressed)
        recyclerViewColor.adapter = filterColorAdapter

        filterFinishAdapter = FilterFinishAdapter(::handleFilterFinishPressed)
        recyclerViewFinish.adapter = filterFinishAdapter
    }

    private fun observeFilteringWattage(filteringWattage: FilteringWattage) {
        if (filteringWattage.isUpdated) {
            filterWattageAdapter.updateBackgroundAppearance(filteringWattage.filteredWattageButtons)
        }
        filterWattageAdapter.filterListWattage = filteringWattage.filteredWattageButtons
    }

    private fun observeFilteringColor(filteringColor: FilteringColor) {
        if (filteringColor.isUpdated) {
            filterColorAdapter.updateBackgroundAppearance(filteringColor.filteredColorButtons)
        }
        filterColorAdapter.filterListColor = filteringColor.filteredColorButtons

    }

    private fun observeFinishWattage(filterFinish: FilteringFinish) {
        if (filterFinish.isUpdated) {
            filterFinishAdapter.updateBackgroundAppearance(filterFinish.filteredFinishButtons)
        }
        filterFinishAdapter.filterListFinish = filterFinish.filteredFinishButtons
    }

    private fun observeProductSelectedResult(productSelectedModel: ProductSelectedModel) {
        textViewWattage.text = String.format(
            getString(R.string.wattage_variation),
            productSelectedModel.productSelected.wattageReplaced.toString()
        )
        textViewColor.text = getColorName(productSelectedModel.productSelected.colorCctCode)
        textViewFinish.text = getFinishName(productSelectedModel.productSelected.productFinishCode)
    }

    private fun navigateBackToDetail(navigationModel: Event<NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            setIntentForResult {
                putParcelableArrayListExtra(
                    PRODUCT_LIST_EXTRA,
                    navModel.categoryProducts.parcelizeProductList()
                )
            }
            finish()
            setAnimation()
        }
    }

    private fun handleFilterWattagePressed(filter: FilterVariationCF) {
        viewModel.onFilterWattageTap(filter)
    }

    private fun handleFilterColorPressed(filter: FilterVariationCF) {
        viewModel.onFilterColorTap(filter)
    }

    private fun handleFilterFinishPressed(filter: FilterVariationCF) {
        viewModel.onFilterFinishTap(filter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setAnimation()
    }

    private fun setAnimation() {
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

}
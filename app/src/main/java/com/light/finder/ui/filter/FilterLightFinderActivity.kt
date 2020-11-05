package com.light.finder.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.di.modules.submodules.FilterComponent
import com.light.finder.di.modules.submodules.FilterModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.logEventOnGoogleTagManager
import com.light.finder.extensions.setIntentForResult
import com.light.finder.ui.browse.BrowseResultFragment.Companion.SORT_SELECTION
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.FilterViewModel
import kotlinx.android.synthetic.main.browse_filter_layout.*
import timber.log.Timber


class FilterLightFinderActivity : BaseLightFinderActivity() {

    companion object {
        const val SORT_ID = "sortId"
        const val RECOMMENDED = 2131362146
        const val LOW_TO_HIGH = 2131362092
        const val HIGH_TO_LOW = 2131362030
        const val FILTER_SCREEN_TAG = "BrowseResultsSort"

    }


    private lateinit var component: FilterComponent
    private val viewModel: FilterViewModel by lazy { getViewModel { component.filterViewModel } }
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        setContentView(R.layout.browse_filter_layout)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        component = app.applicationComponent.plus(FilterModule())

        val intent: Intent = intent
        val sortId = intent.getIntExtra(SORT_SELECTION, RECOMMENDED)

        selectOption(sortId)

        setRadioButtonClickListener()
        navigationObserver()
    }

    private fun selectOption(sortId: Int?) {
        when (sortId) {
            LOW_TO_HIGH -> lowToHigh.isChecked = true
            HIGH_TO_LOW -> highToLow.isChecked = true
            else -> recommended.isChecked = true
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(this, FILTER_SCREEN_TAG, null)
    }

    private fun setRadioButtonClickListener() {

        sort_by_radiogroup.setOnCheckedChangeListener { group, checkedId ->

            val checkedRadioButton =
                group.findViewById<View>(checkedId) as RadioButton

            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.browse_sort_results)) {}
                when (checkedId) {
                    R.id.recommended -> viewModel.onFilterClick(RECOMMENDED)
                    R.id.lowToHigh -> viewModel.onFilterClick(LOW_TO_HIGH)
                    R.id.highToLow -> viewModel.onFilterClick(HIGH_TO_LOW)
                }
                Timber.d("ege $checkedId ${checkedRadioButton.id}")
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setAnimation()
    }


    private fun navigationObserver() {
        viewModel.modelNavigation.observe(
            this,
            Observer(::navigateBackToBrowseResults)
        )
    }

    private fun navigateBackToBrowseResults(navigationModel: Event<FilterViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            setIntentForResult {
                putExtra(SORT_ID, navModel.filterId)
            }
            finish()
            setAnimation()
        }
    }

    private fun setAnimation() {
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }


}

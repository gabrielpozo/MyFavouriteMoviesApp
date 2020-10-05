package com.light.finder.ui.filter

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
import com.light.finder.extensions.setIntentForResult
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.FilterViewModel
import kotlinx.android.synthetic.main.browse_filter_layout.*
import timber.log.Timber


class FilterLightFinderActivity : BaseLightFinderActivity() {

    companion object {
        const val SORT_ID = "sortId"
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



        setRadioButtonClickListener()
        navigationObserver()
    }

    private fun setRadioButtonClickListener() {

        sort_by_radiogroup.setOnCheckedChangeListener { group, checkedId ->

            val checkedRadioButton =
                group.findViewById<View>(checkedId) as RadioButton

            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                viewModel.onFilterClick(checkedId)
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

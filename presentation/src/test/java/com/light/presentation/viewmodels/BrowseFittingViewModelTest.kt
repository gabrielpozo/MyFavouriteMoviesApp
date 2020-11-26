package com.light.presentation.viewmodels

import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.light.presentation.*
import com.light.presentation.common.Event
import com.light.usecases.GetFormFactorsEditBrowseUseCase
import com.light.usecases.RequestBrowsingFittingsUseCase
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BrowseFittingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var requestBrowsingProductsUseCase: RequestBrowsingFittingsUseCase

    @Mock
    lateinit var getFormFactorsEditBrowseUseCase: GetFormFactorsEditBrowseUseCase

    @Mock
    lateinit var observerBrowsingModel: Observer<BrowseFittingViewModel.UiBrowsingModel>

    @Mock
    lateinit var observerModelNavigation: Observer<Event<BrowseFittingViewModel.NavigationToShapeFiltering>>

    private lateinit var vm: BrowseFittingViewModel


    @Before
    fun setUp() {
        vm = BrowseFittingViewModel(
            requestBrowsingProductsUseCase,
            getFormFactorsEditBrowseUseCase,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `when requesting browsing fitting products then, a form-factor list is returned successfully`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)
            whenever(
                requestBrowsingProductsUseCase.execute(
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(formFactorList)
            }

            vm.onRequestBrowsingProducts()

            verify(observerBrowsingModel).onChanged(
                BrowseFittingViewModel.UiBrowsingModel.SuccessRequestStatus(
                    formFactorList
                )
            )
        }
    }

    @Test
    fun `when requesting browsing fitting products, then a general error is returned`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)
            whenever(
                requestBrowsingProductsUseCase.execute(
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnError(errorMessage)
            }

            vm.onRequestBrowsingProducts()

            verify(observerBrowsingModel).onChanged(
                BrowseFittingViewModel.UiBrowsingModel.ErrorRequestStatus(
                    errorMessage
                )
            )
        }
    }


    @Test
    fun `when requesting browsing fitting products from local, then a form-factor list is returned successfully`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)
            whenever(getFormFactorsEditBrowseUseCase.execute()).thenReturn(formFactorSavedList)


            vm.onRequestSavedFormFactorList()

            verify(observerBrowsingModel).onChanged(
                BrowseFittingViewModel.UiBrowsingModel.SuccessRequestStatus(
                    formFactorSavedList
                )
            )
        }
    }

    @Test
    fun `when requesting v, then loading status is shown`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)

            vm.onRequestBrowsingProducts()

            verify(observerBrowsingModel).onChanged(
                BrowseFittingViewModel.UiBrowsingModel.LoadingStatus
            )
        }
    }


    @Test
    fun `when clicking on next button and there is a form-factor already selected, then navigates to shape screen`() {
        vm.modelNavigationShape.observeForever(observerModelNavigation)

        vm.onFittingClick(productFormFactor1)
        vm.onNextButtonPressed()

        verify(observerModelNavigation).onChanged(
            Event(
                BrowseFittingViewModel.NavigationToShapeFiltering(productFormFactor1)
            )
        )
    }

    @Test
    fun `when clicking a new form-factor different to the form-factor already clicked, then navigates to a shape screen with the current selected fitting`() {
        vm.modelNavigationShape.observeForever(observerModelNavigation)

        vm.onFittingClick(productFormFactor1)
        vm.onFittingClick(productFormFactor2)
        vm.onNextButtonPressed()

        verify(observerModelNavigation).onChanged(
            Event(
                BrowseFittingViewModel.NavigationToShapeFiltering(productFormFactor2)
            )
        )
    }

    @Test
    fun `when clicking a next button and not form-factor is selected, then next button performs no action`() {
        vm.modelNavigationShape.observeForever(observerModelNavigation)

        vm.onNextButtonPressed()

        verifyNoMoreInteractions(observerModelNavigation)
    }

}

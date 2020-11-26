package com.light.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.light.presentation.*
import com.light.presentation.common.Event
import com.light.usecases.GetShapeEditBrowseUseCase
import com.light.usecases.RequestBrowsingShapeUseCase
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
class BrowseShapeViewModelTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var requestBrowsingShapeUseCase: RequestBrowsingShapeUseCase

    @Mock
    lateinit var getShapeEditBrowseUseCase: GetShapeEditBrowseUseCase

    @Mock
    lateinit var observerBrowsingModel: Observer<BrowseShapeViewModel.UiBrowsingShapeModel>

    @Mock
    lateinit var observerStatusBottomModel: Observer<BrowseShapeViewModel.StatusBottomBar>

    @Mock
    lateinit var observerModelNavigation: Observer<Event<BrowseShapeViewModel.NavigationToResults>>

    private lateinit var vm: BrowseShapeViewModel


    @Before
    fun setUp() {
        vm = BrowseShapeViewModel(
            requestBrowsingShapeUseCase,
            getShapeEditBrowseUseCase,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `when requesting filtering shapes then a list of fitting shapes is returned successfully`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)

            verify(observerBrowsingModel).onChanged(
                BrowseShapeViewModel.UiBrowsingShapeModel.SuccessRequestStatus(shapeBrowsingList)
            )
        }
    }

    @Test
    fun `when requesting filtering shapes then loading status is shown`() {
        runBlocking {
            vm.modelBrowsingLiveData.observeForever(observerBrowsingModel)

            vm.onRetrievingShapeList(false, productFormFactor1)

            verify(observerBrowsingModel).onChanged(
                BrowseShapeViewModel.UiBrowsingShapeModel.LoadingStatus
            )
        }
    }


    @Test
    fun `when clicking on reset button and shapes have been retrieved already, reset state is sent to the view`() {
        runBlocking {
            vm.modelBottomStatus.observeForever(observerStatusBottomModel)

            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onResetButtonPressed()

            verify(observerStatusBottomModel).onChanged(
                BrowseShapeViewModel.StatusBottomBar.ResetShape
            )
        }
    }

    @Test
    fun `when clicking on search button and there is a shape already selected, navigate to result page event is sent to the view`() {
        runBlocking {
            vm.modelNavigationToResult.observeForever(observerModelNavigation)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onShapeClick(shapeBrowseClicked)
            vm.onSearchButtonClicked()


            verify(observerModelNavigation).onChanged(
                Event(
                    BrowseShapeViewModel.NavigationToResults(
                        shapeBrowsingList,
                        productFormFactor1.id,
                        productFormFactor1.name
                    )
                )
            )
        }
    }

    @Test
    fun `when clicking on search button and there is no shape selected yet, then search button performs no action`() {
        runBlocking {
            vm.modelNavigationToResult.observeForever(observerModelNavigation)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onSearchButtonClicked()

            verifyZeroInteractions(observerModelNavigation)
        }
    }


    @Test
    fun `when clicking on a shape filter card, then shape-clicked event is triggered`() {
        runBlocking {
            vm.modelBottomStatus.observeForever(observerStatusBottomModel)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onShapeClick(shapeBrowseClicked)

            verify(observerStatusBottomModel).onChanged(
                BrowseShapeViewModel.StatusBottomBar.ShapeClicked
            )
        }
    }


    @Test
    fun `when clicking on a shape filter card and it is already selected then no-buttons-clicked event is triggered`() {
        runBlocking {
            vm.modelBottomStatus.observeForever(observerStatusBottomModel)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }

            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onShapeClick(shapeAlreadySelected)

            verify(observerStatusBottomModel).onChanged(
                BrowseShapeViewModel.StatusBottomBar.NoButtonsClicked
            )
        }
    }


    @Test
    fun `when clicking on a skip button then navigate to choice category page`() {
        runBlocking {
            vm.modelNavigationToResult.observeForever(observerModelNavigation)
            whenever(
                requestBrowsingShapeUseCase.execute(
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(shapeBrowsingList)
            }
            vm.onRetrievingShapeList(false, productFormFactor1)
            vm.onSkipButtonClicked()

            verify(observerModelNavigation).onChanged(
                Event(
                    BrowseShapeViewModel.NavigationToResults(
                        shapeBrowsingList,
                        productFormFactor1.id,
                        productFormFactor1.name
                    )
                )
            )
        }
    }


}

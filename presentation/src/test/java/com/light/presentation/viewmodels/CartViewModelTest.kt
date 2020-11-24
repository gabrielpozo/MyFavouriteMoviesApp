package com.light.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.light.presentation.*
import com.light.presentation.common.Event
import com.light.usecases.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getItemCountUseCase: GetItemCountUseCase

    @Mock
    lateinit var observerModelContent: Observer<CartViewModel.CountItemsModel>

    @Mock
    lateinit var observerModelNetwork: Observer<CartViewModel.NetworkModel>

    @Mock
    lateinit var observerModelContentReload: Observer<CartViewModel.ContentReload>


    private lateinit var vm: CartViewModel

    @Before
    fun setUp() {
        vm = CartViewModel(
            getItemCountUseCase,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `when requesting how many items the cart has, then no items is returned`() {
        runBlocking {
            vm.modelItemCountRequest.observeForever(observerModelContent)
            whenever(
                getItemCountUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(noItemsCount)
            }

            vm.onRequestGetItemCount()

            verify(observerModelContent).onChanged(CartViewModel.CountItemsModel.ClearedBadgeItemCount)
        }
    }

    @Test
    fun `when requesting how many items the cart has, then multiple items are returned`() {
        runBlocking {
            vm.modelItemCountRequest.observeForever(observerModelContent)
            whenever(
                getItemCountUseCase.execute(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(multipleItemsCount)
            }

            vm.onRequestGetItemCount()

            verify(observerModelContent).onChanged(
                CartViewModel.CountItemsModel.RequestModelItemCount(
                    Event(multipleItemsCount)
                )
            )
        }
    }

    @Test
    fun `when checking network connection, then we get network offline status`() {
        vm.modelNetworkConnection.observeForever(observerModelNetwork)
        vm.onCheckNetworkConnection(getNetworkStatusOffline())
        verify(observerModelNetwork).onChanged(CartViewModel.NetworkModel.NetworkOffline)

    }

    @Test
    fun `when checking network connection, then we get network online status`() {
        vm.modelNetworkConnection.observeForever(observerModelNetwork)
        vm.onCheckNetworkConnection(getNetworkStatusOnline())
        verify(observerModelNetwork).onChanged(CartViewModel.NetworkModel.NetworkOnline)
    }

    @Test
    fun `when getting success web url, then we sent payment has been done successfully to the view`() {
        vm.modelItemCountRequest.observeForever(observerModelContent)
        vm.onSetWebUrl(URL_SUCCESS)
        verify(observerModelContent).onChanged(CartViewModel.CountItemsModel.PaymentSuccessful)
    }

    @Test
    fun `when not getting success web url, then we don't send anything to the view`() {
            vm.modelItemCountRequest.observeForever(observerModelContent)
            vm.onSetWebUrl(URL_NOT_SUCCESS)
            verifyZeroInteractions(observerModelContent)
    }

    @Test
    fun `when checking process on web view, then we get check-out process`() {
        vm.modelReload.observeForever(observerModelContentReload)
        vm.onSetWebUrl(URL_CHECKOUT_PROCESS)
        vm.onCheckReloadCartWebView(getItemsAddedForCheckingProcess())
        verify(observerModelContentReload).onChanged(CartViewModel.ContentReload.ContentOnCheckProcess)
    }

    @Test
    fun `when checking process on one-page web view, then we get check-out process`() {
        vm.modelReload.observeForever(observerModelContentReload)
        vm.onSetWebUrl(URL_CHECKOUT_PROCESS)
        vm.onCheckReloadCartWebView(getItemsAddedForCheckingProcess())
        verify(observerModelContentReload).onChanged(CartViewModel.ContentReload.ContentOnCheckProcess)
    }

    @Test
    fun `when checking process on web view and not one-page or checking url ,then we send the action of content to be loaded`() {
        vm.modelReload.observeForever(observerModelContentReload)
        vm.onCheckReloadCartWebView(getItemsAddedForCheckingProcess())
        verify(observerModelContentReload).onChanged(CartViewModel.ContentReload.ContentToBeLoaded)
    }
}





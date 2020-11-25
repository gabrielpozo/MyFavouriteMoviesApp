package com.light.presentation

import com.light.domain.model.*

//General
const val errorMessage = "Error default message"

//Cart
fun getNetworkStatusOnline() = true
fun getNetworkStatusOffline() = false
fun getItemsAddedForCheckingProcess() = false
val noItemsCount = CartItemCount(0, 0)
val multipleItemsCount = CartItemCount(4, 4)
val URL_SUCCESS = "/onepage/success/"
val URL_NOT_SUCCESS = ""
val URL_CHECKOUT_PROCESS = "/checkout/cart/"
val URL_CHECKOUT_ONE_PAGE = "/checkout/onepage/"
val NOT_CONTENT = ""

//Browse Fitting
val productFormFactor1: FormFactorTypeBaseId = FormFactorTypeBaseId(
    1,
    "name",
    "",
    "",
    3
)
val productFormFactor2: FormFactorTypeBaseId = FormFactorTypeBaseId(
    2,
    "name",
    "",
    "",
    3
)
val productFormFactor3: FormFactorTypeBaseId = FormFactorTypeBaseId(
    1,
    "name",
    "",
    "",
    3,
    isSelected = true
)

val formFactorList = listOf<FormFactorTypeBaseId>()
val formFactorSavedList = listOf(productFormFactor1, productFormFactor2, productFormFactor3)
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


//Browse Shape
val shapeBrowse1 = ShapeBrowsing(1, "", "", 1, 1, 1, "")
val shapeBrowse2 = ShapeBrowsing(2, "", "", 2, 1, 1, "")
val shapeBrowse3 = ShapeBrowsing(3, "", "", 3, 1, 1, "")
val shapeBrowse4 = ShapeBrowsing(4, "", "", 4, 1, 1, "")


val shapeBrowseClicked = ShapeBrowsing(1, "", "", 4, 1, 1, "", true)

val shapeAlreadySelected = ShapeBrowsing(1, "", "", 4, 1, 1, "", false)

val shapeBrowsingList = listOf(shapeBrowse1, shapeBrowse2, shapeBrowse3, shapeBrowse4)
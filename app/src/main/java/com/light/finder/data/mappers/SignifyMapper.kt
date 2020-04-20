package com.light.finder.data.mappers

import com.light.domain.model.*
import com.light.finder.data.source.remote.*

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories?.map { categoryDto ->
        if (categoryDto.categoryProducts?.isNotEmpty() == true) {
            categoriesList.add(
                Category(
                    categoryProductBase = categoryDto.categoryProductBase ?: "",
                    categoryProducts = categoryDto.categoryProducts?.map(mapServerProductToDomain),
                    categoryName = categoryDto.categoryName ?: "",
                    categoryIndex = categoryDto.categoryIndex ?: 0,
                    categoryImage = categoryDto.categoryImage ?: "",
                    priceRange = getMinMaxPriceTag(
                        categoryDto.categoryPrice?.minPrice,
                        categoryDto.categoryPrice?.maxPrice
                    ),
                    minWattage = categoryDto.categoryWattReplace?.let { list ->
                        if (list.isNotEmpty()) {
                            list[0].toString()
                        } else ""
                    } ?: "",
                    maxWattage = categoryDto.categoryWattReplace?.let { list ->
                        if (list.isNotEmpty() && list.size > 1) {
                            list[1].toString()
                        } else ""
                    } ?: "",
                    maxEnergySaving = categoryDto.categoryEnergySave?.maxEnergySaving ?: 0.0f,
                    minEnergySaving = categoryDto.categoryEnergySave?.minEnergySaving ?: 0.0f,
                    colors = categoryDto.categoryCctCode?.map { it } ?: emptyList(),
                    finishCodes = categoryDto.categoryFilterFinishCode?.map { it } ?: emptyList()
                )
            )
        }
    }

    Message(
        categories = categoriesList.sortedBy { it.categoryIndex }
    )
}

private val mapServerProductToDomain: (ProductDto) -> Product = { productDto ->
    Product(
        name = productDto.name ?: "",
        index = productDto.index ?: 0,
        spec1 = productDto.spec1 ?: 0.0f,
        spec3 = productDto.spec3 ?: emptyList(),
        spec2 = productDto.spec2 ?: "",
        imageUrls = productDto.imageUrls ?: emptyList(),
        description = productDto.description ?: "",
        scene = productDto.scene ?: "",
        categoryName = productDto.categoryName ?: "",
        sapID12NC = productDto.sapID12NC ?: 0,
        qtyLampscase = productDto.qtyLampscase ?: 0,
        wattageReplaced = productDto.wattageReplaced ?: 0,
        country = productDto.country ?: "",
        priority = productDto.priority ?: 0,
        wattageClaim = productDto.wattageClaim ?: 0.0f,
        factorBase = productDto.factorBase ?: "",
        discountProc = productDto.discountProc ?: 0,
        sapID10NC = productDto.sapID10NC ?: 0,
        dimmingCode = productDto.dimmingCode ?: 0,
        finish = productDto.finish ?: "",
        promoted = productDto.promoted ?: 0,
        priceSku = productDto.priceSku ?: 0.0f,
        priceLamp = productDto.priceLamp ?: 0.0f,
        pricePack = productDto.pricePack ?: 0.0f,
        factorShape = productDto.factorShape ?: "",
        qtyLampSku = productDto.qtyLampSku ?: 0,
        discountValue = productDto.discountValue ?: 0,
        qtySkuCase = productDto.qtySkuCase ?: 0,
        factorTypeCode = productDto.factorTypeCode ?: 0,
        colorCctCode = productDto.productCctCode ?: 0,
        formfactorType = productDto.factorTypeCode ?: 0,
        productFinishCode = productDto.productFinishCode ?: 0

    )
}


val mapCartToDomain: (CartResultDto) -> Cart = { cartDto ->

    Cart(
        success = cartDto.success ?: "",
        error = cartDto.error ?: "",
        product = mapCartProductToDomain(cartDto.product!!)
    )

}

private val mapCartProductToDomain: (CartProductDto) -> CartProduct = { cartDto ->

    CartProduct(
        name = cartDto.name ?: ""
    )

}


val mapCartItemCountToDomain: (CartItemCountResultDto) -> CartItemCount = { countResultDto ->

    CartItemCount(
        itemCount = countResultDto.itemsCount ?: 0,
        itemQuantity = countResultDto.itemsQuantity ?: 0
    )
}


fun getMinMaxPriceTag(minPrice: Float?, maxPrice: Float?): String =
    if (minPrice == null || maxPrice == null) {
        "-"

    } else if (minPrice == maxPrice && minPrice != 0.0f) {
        "$$minPrice"

    } else {
        "$$minPrice-$$maxPrice"
    }


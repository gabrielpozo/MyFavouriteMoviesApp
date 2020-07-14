package com.light.finder.data.mappers

import com.light.domain.model.*
import com.light.finder.data.source.remote.*
import com.light.finder.data.source.remote.dto.*

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories?.map { categoryDto ->
        if (categoryDto.categoryProducts.isNotEmpty()) {
            categoriesList.add(
                Category(
                    categoryProductBase = categoryDto.categoryProductBase,
                    categoryProducts = categoryDto.categoryProducts.map(mapServerProductToDomain),
                    categoryName = categoryDto.categoryName,
                    categoryIndex = categoryDto.categoryIndex,
                    categoryImage = categoryDto.categoryImage,
                    priceRange = getMinMaxPriceTag(
                        categoryDto.categoryPrice?.minPrice,
                        categoryDto.categoryPrice?.maxPrice
                    ),
                    categoryWattReplaced = categoryDto.categoryWattReplace,
                    maxEnergySaving = categoryDto.categoryEnergySave.maxEnergySaving,
                    minEnergySaving = categoryDto.categoryEnergySave.minEnergySaving,
                    colors = categoryDto.categoryCctCode.map { it },
                    finishCodes = categoryDto.categoryFilterFinishCode.map { it },
                    categoryShape = categoryDto.categoryProductShape
                )
            )
        }
    }

    Message(
        //TODO move sortedBy to repository
        categories = categoriesList.sortedBy { it.categoryIndex },
        version = messageDto.version,
        baseIdentified = messageDto.baseIdentified,
        formfactorType = messageDto.formfactorType,
        shapeIdentified = messageDto.shape_identified
    )
}

private val mapServerProductToDomain: (ProductDto) -> Product = { productDto ->
    Product(
        name = productDto.name,
        index = productDto.index,
        spec1 = productDto.spec1,
        spec3 = productDto.spec3,
        spec2 = productDto.spec2,
        imageUrls = productDto.imageUrls,
        description = productDto.description,
        scene = productDto.scene,
        categoryName = productDto.categoryName,
        sapID12NC = productDto.sapID12NC,
        qtyLampscase = productDto.qtyLampscase,
        wattageReplaced = productDto.wattageReplaced,
        country = productDto.country,
        priority = productDto.priority,
        wattageClaim = productDto.wattageClaim,
        factorBase = productDto.factorBase,
        discountProc = productDto.discountProc,
        sapID10NC = productDto.sapID10NC,
        dimmingCode = productDto.dimmingCode,
        finish = productDto.finish,
        promoted = productDto.promoted,
        priceSku = productDto.priceSku,
        priceLamp = productDto.priceLamp,
        pricePack = productDto.pricePack,
        factorShape = productDto.factorShape,
        qtyLampSku = productDto.qtyLampSku,
        discountValue = productDto.discountValue,
        qtySkuCase = productDto.qtySkuCase,
        factorTypeCode = productDto.factorTypeCode,
        colorCctCode = productDto.productCctCode,
        formfactorType = productDto.factorTypeCode,
        productFinishCode = productDto.productFinishCode
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


val mapLegendToDomain: (LegendParsingDto) -> LegendParsing = { legendDto ->
    LegendParsing(
        legend = mapLegendValueToDomain(legendDto.legend)

    )
}

private val mapLegendValueToDomain: (LegendValueDto) -> LegendValue = { legendValueDto ->
    LegendValue(
        productFormFactorType = legendValueDto.productFormFactorType.map {
            FormFactorType(
                it.id,
                it.name,
                it.image,
                it.order
            )
        },
        finishType = legendValueDto.productFinish.map {
            FinishType(
                it.id,
                it.name,
                it.image,
                it.order
            )
        }
        , cctType = legendValueDto.productCctName.map {
            CctType(
                it.id,
                it.name,
                it.smallIcon,
                it.bigIcon,
                it.order,
                it.arType,
                MAP_KELVIN_DTO_TO_DOMAIN(it.kelvinSpec)
            )
        }
    )
}

private val MAP_KELVIN_DTO_TO_DOMAIN: (KelvinSpecDto) -> KelvinSpec =
    {
        KelvinSpec(
            it.minValue,
            it.maxValue,
            it.defaultValue
        )
    }

fun getMinMaxPriceTag(minPrice: Float?, maxPrice: Float?): String =
    if (minPrice == null || maxPrice == null) {
        "-"
    } else if (minPrice == maxPrice && minPrice != 0.0f) {
        priceTransform(minPrice)
    } else {
        "${priceTransform(minPrice)}-${priceTransform(maxPrice)}"
    }

fun priceTransform(value: Float): String {
    return "$%.2f".format(value)
}
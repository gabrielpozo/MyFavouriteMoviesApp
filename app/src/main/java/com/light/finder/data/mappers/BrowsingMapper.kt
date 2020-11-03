package com.light.finder.data.mappers

import com.light.domain.model.*
import com.light.finder.data.source.remote.dto.ProductBrowsingListDto
import com.light.finder.extensions.addOrderField


private const val EMPTY_STRING = ""
val mapProductsBrowsingToDomain: (ProductBrowsingListDto) -> List<ProductBrowsing> =
    { productsBrowsingListDto ->
        val productBrowsingList: ArrayList<ProductBrowsing> = ArrayList()
        productsBrowsingListDto.product_list.map {
            productBrowsingList.add(
                ProductBrowsing(
                    productCountry = it.product_country,
                    productPromoted = it.product_promoted,
                    productDiscountProc = it.product_discount_proc,
                    productDiscountValue = it.product_discount_value,
                    productFormfactorBase = it.product_formfactor_base,
                    productFormfactorShape = it.product_formfactor_shape,
                    productFinishCode = it.product_finish_code,
                    productCctCode = it.product_cct_code,
                    productDimming_code = it.product_dimming_code,
                    productFinish = it.product_finish,
                    productName = it.product_name,
                    productPrio = it.product_prio,
                    productCategoryName = it.product_category_name,
                    productWattageClaim = it.product_wattage_claim,
                    productWattageReplaced = it.product_wattage_replaced,
                    productWattageReplacedExtra = it.productWattageReplacedExtra,
                    productPricePack = it.product_price_pack,
                    productPriceSku = it.product_price_sku,
                    productPriceLamp = it.product_price_lamp,
                    productSAPid10NC = it.product_SAPid_10NC,
                    productSAPid12NC = it.product_SAPid_12NC,
                    productQtyLampsku = it.product_qty_lampsku,
                    productQtySkucase = it.product_qty_skucase,
                    productQtyLampscase = it.product_qty_lampscase,
                    productDescription = it.product_description,
                    productFormfactorTypeCode = it.product_formfactor_type_code,
                    productFormfactorType = it.product_formfactor_type,
                    productFormfactorShapeId = it.product_formfactor_shape_id,
                    productFormfactorBaseId = it.product_formfactor_base_id,
                    productCategoryCode = it.product_category_code,
                    energyConsumption = it.energy_consumption,
                    productConnectionCode = it.product_connection_code,
                    productScene = it.product_scene,
                    productSceneImage = it.product_scene_image,
                    productSceneXoptions = it.product_scene_x_options,
                    productSceneYoptions = it.product_scene_y_options,
                    productDetailsIcons = it.product_details_icons,
                    productDetailsIconsImages = it.product_details_icons_images,
                    productIndex = it.product_index,
                    productImage = it.product_image,
                    productSpec1 = it.product_spec1,
                    productSpec2 = it.product_spec2,
                    productSpec3 = it.product_spec3,
                    stickyHeaderFirstLine = it.stickyHeaderFirstLine,
                    stickyHeaderSecondLine = it.stickyHeaderSecondLine
                )
            )
        }

        productBrowsingList
    }

val mapBrowsingProductToMessageDomain: (String, List<ProductCategoryName>, List<FormFactorType>, Map<Key, List<ProductBrowsing>>) -> Message =
    { fittingString, productCategoryNameList, formFactorType, productBrowsingHashMap ->
        //we get the first value
        if (productBrowsingHashMap.isNotEmpty()) {
            val categories = productBrowsingHashMap.map {
                mapBrowsingCategoryToDomain(formFactorType, it.value)
            }.also { categoryList ->
                categoryList.forEach { category ->
                    category.addOrderField(productCategoryNameList)
                }
            }

            Message(
                categories = categories.sortedBy { it.order },
                version = EMPTY_STRING,
                baseIdentified = EMPTY_STRING,
                formfactorType = EMPTY_STRING,
                shapeIdentified = fittingString,
                textIdentified = EMPTY_STRING,
                imageIdentified = EMPTY_STRING
            )
        } else {
            Message(
                categories = emptyList(),
                version = EMPTY_STRING,
                formfactorType = EMPTY_STRING,
                baseIdentified = EMPTY_STRING,
                shapeIdentified = fittingString,
                textIdentified = EMPTY_STRING,
                imageIdentified = EMPTY_STRING
            )
        }
    }


val mapBrowsingCategoryToDomain: (List<FormFactorType>, List<ProductBrowsing>) -> Category =
    { productFormFactorType, productBrowsingList ->

        Category(
            categoryProductBase = if (productBrowsingList.isNotEmpty()) {
                productBrowsingList[0].productFormfactorBase
            } else {
                ""
            },
            categoryProducts = productBrowsingList.map(mapBrowsingProductToProductDomain),
            categoryIndex = productBrowsingList[0].productCategoryCode,
            categoryName = EMPTY_STRING,
            categoryImage = EMPTY_STRING,
            priceRange = getMinMaxPriceTag(
                productBrowsingList.minBy { it.productPriceLamp }?.productPriceLamp,
                productBrowsingList.maxBy { it.productPriceLamp }?.productPriceLamp
            ),
            maxPrice = productBrowsingList.maxBy { it.productPriceLamp }?.productPriceLamp ?: 0.0f,
            minPrice = productBrowsingList.minBy { it.productPriceLamp }?.productPriceLamp ?: 0.0f,
            categoryWattReplaced = productBrowsingList.map { it.productWattageReplaced }.distinct(),
            maxEnergySaving = 0.0f,
            minEnergySaving = 0.0f,

            colors = productBrowsingList.map {
                it.productCctCode
            }.distinct(),
            finishCodes = productBrowsingList.map {
                it.productFinishCode
            }.distinct(),

            categoryShape =
            productFormFactorType.find {
                productBrowsingList[0].productFormfactorTypeCode == it.id
            }?.name ?: EMPTY_STRING,
            categoryDescription = EMPTY_STRING,
            categoryConnectivityCode = emptyList()
        )
    }

val mapBrowsingProductToProductDomain: (ProductBrowsing) -> Product =
    { productBrowse ->
        Product(
            name = productBrowse.productName,
            categoryName = productBrowse.productCategoryName,
            factorShape = productBrowse.productFormfactorShape,
            factorBase = productBrowse.productFormfactorBase,
            colorCctCode = productBrowse.productCctCode,
            wattageReplaced = productBrowse.productWattageReplaced,
            formfactorType = productBrowse.productFormfactorTypeCode,
            description = productBrowse.productDescription,
            pricePack = productBrowse.productPricePack,
            sapID10NC = productBrowse.productSAPid10NC,
            sapID12NC = productBrowse.productSAPid12NC,
            priceLamp = productBrowse.productPriceLamp,
            qtySkuCase = productBrowse.productQtySkucase,
            qtyLampscase = productBrowse.productQtyLampscase,
            imageUrls = productBrowse.productImage,
            productFinishCode = productBrowse.productFinishCode,
            factorTypeCode = productBrowse.productFormfactorTypeCode,
            priceSku = productBrowse.productPriceSku,
            qtyLampSku = productBrowse.productQtyLampsku,
            produtCategoryCode = productBrowse.productCategoryCode,
            wattageReplacedExtra = productBrowse.productWattageReplacedExtra,
            productConnectionCode = productBrowse.productConnectionCode,
            productPrio = productBrowse.productPrio,
            stickyHeaderFirstLine = productBrowse.stickyHeaderFirstLine,
            stickyHeaderSecondLine = productBrowse.stickyHeaderSecondLine
        )
    }

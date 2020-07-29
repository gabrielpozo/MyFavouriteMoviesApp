package com.light.finder.data.mappers

import com.light.domain.model.*
import com.light.finder.data.source.remote.dto.ProductBrowsingListDto


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
                    productSpec3 = it.product_spec3
                )
            )
        }

        productBrowsingList
    }

val mapBrowsingProductToMessageDomain: (String, Map<Key, List<ProductBrowsing>>) -> Message =
    { fittingString, productBrowsingHashMap ->
        //we get the first value
        val entry = productBrowsingHashMap.entries.iterator().next()
        val listBrowsing = entry.value
        Message(
            categories = productBrowsingHashMap.map {
                mapBrowsingCategoryToDomain(it.value)
            },
            version = EMPTY_STRING,
            baseIdentified = fittingString,
            formfactorType = EMPTY_STRING,
            shapeIdentified = listBrowsing[0].productFormfactorShape,
            textIdentified = EMPTY_STRING
        )
    }


val mapBrowsingCategoryToDomain: (List<ProductBrowsing>) -> Category =
    { productBrowsingList ->

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
            categoryWattReplaced = productBrowsingList.map { it.productWattageReplaced }.distinct(),
            maxEnergySaving = 0.0f,
            minEnergySaving = 0.0f,

            colors = productBrowsingList.map {
                it.productCctCode
            }.distinct(),
            finishCodes = productBrowsingList.map {
                it.productFinishCode
            }.distinct(),

            categoryShape = if (productBrowsingList.isNotEmpty()) {
                productBrowsingList[0].productFormfactorShape
            } else {
                EMPTY_STRING
            }
            , categoryDescription = EMPTY_STRING
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
            wattageReplacedExtra = productBrowse.productWattageReplacedExtra
        )
    }

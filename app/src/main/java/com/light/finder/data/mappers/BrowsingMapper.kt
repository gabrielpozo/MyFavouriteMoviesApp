package com.light.finder.data.mappers

import com.light.domain.model.ProductBrowsing
import com.light.finder.data.source.remote.dto.ProductBrowsingDto


val mapProductsBrowsingToDomain: (List<ProductBrowsingDto>) -> List<ProductBrowsing> =
    { productsBrowsingDto ->
        val productBrowsingList: ArrayList<ProductBrowsing> = ArrayList()
        productsBrowsingDto.map {
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
                    productImageNoExten = it.product_image_no_exten,
                    productName = it.product_name,
                    productPrio = it.product_prio,
                    productCategoryName = it.product_category_name,
                    productWattageClaim = it.product_wattage_claim,
                    productWattageReplaced = it.product_wattage_replaced,
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
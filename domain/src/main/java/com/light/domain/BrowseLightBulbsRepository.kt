package com.light.domain

import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.state.DataState


interface BrowseLightBulbsRepository {
    suspend fun getFittingBrowsingProducts(): DataState<List<FormFactorTypeBaseId>>
    suspend fun getFittingListForEditBrowse(): List<FormFactorTypeBaseId>

}
package com.light.finder.data.source.local.db

import androidx.room.*
import com.light.domain.model.ProductBrowsing

@Entity(tableName = DbConstant.TABLE_NAME)
data class BrowseDb (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val productList : List<ProductBrowsing>
)
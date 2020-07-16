package com.light.finder.data.source.local.db

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.light.finder.data.source.local.db.typeconverter.ProductListConverter
import com.light.finder.data.source.remote.dto.ProductDto

@Entity(tableName = DbConstant.TABLE_NAME)
data class BrowseDb (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @Embedded
    @TypeConverters(ProductListConverter::class)
    @SerializedName("product_list")
    val productList : List<ProductDto>
)
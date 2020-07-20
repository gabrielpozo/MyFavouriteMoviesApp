package com.light.finder.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.light.finder.data.source.local.db.dao.BrowseDao
import com.light.finder.data.source.local.db.typeconverter.ProductListConverter

@Database(entities = [BrowseDb::class], version = 1)
@TypeConverters(ProductListConverter::class)
abstract class BrowseRoomDataBase : RoomDatabase() {
    abstract fun browseDao(): BrowseDao
    companion object {
        @Volatile
        private var INSTANCE: BrowseRoomDataBase? = null
        fun getDatabase(context: Context): BrowseRoomDataBase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BrowseRoomDataBase::class.java,
                        DbConstant.DB_NAME
                    ).build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}
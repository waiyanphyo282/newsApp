package com.waiyanphyo.betternews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waiyanphyo.betternews.data.local.daos.ArticleDao
import com.waiyanphyo.betternews.data.local.daos.ArticleKeysDao
import com.waiyanphyo.betternews.data.local.entities.ArticleEntity
import com.waiyanphyo.betternews.data.local.entities.ArticleKeys

@Database(entities = [ArticleEntity::class, ArticleKeys::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    abstract fun articleKeysDao(): ArticleKeysDao
}
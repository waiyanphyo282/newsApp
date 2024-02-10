package com.waiyanphyo.betternews.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waiyanphyo.betternews.data.local.entities.ArticleKeys

@Dao
interface ArticleKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ArticleKeys>)

    @Query("SELECT * FROM article_keys WHERE url = :url")
    suspend fun articleKeysByUrl(url: String): ArticleKeys?

    @Query("DELETE FROM article_keys")
    suspend fun clearArticleKeys()
}
package com.waiyanphyo.betternews.data.local.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.waiyanphyo.betternews.data.local.entities.ArticleEntity
import com.waiyanphyo.betternews.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Upsert()
    suspend fun upsert(article: ArticleEntity): Long

    @Query("select * from articles")
    fun getAllArticles(): PagingSource<Int, ArticleEntity>

    @Query("select * from articles where id=:id")
    fun getArticleById(id: Int): Flow<ArticleEntity>

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("delete from articles")
    fun clearArticles()

    @Upsert
    abstract fun insertAll(articles: List<ArticleEntity>)
}
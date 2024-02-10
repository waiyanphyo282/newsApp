package com.waiyanphyo.betternews.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waiyanphyo.betternews.data.model.Article
import com.waiyanphyo.betternews.data.model.Source

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String?,
    val content: String,
    val description: String = "",
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?
)

fun ArticleEntity.asExternalModel() = Article(
    id,
    author,
    content,
    description,
    publishedAt,
    source,
    title,
    url,
    urlToImage
)
package com.waiyanphyo.betternews.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_keys")
data class ArticleKeys(
    @PrimaryKey
    val url: String,
    val prevKey: Int?,
    val nextKey: Int?,
)

package com.waiyanphyo.betternews.data.model

data class Article(
    val id: Int?,
    val author: String?,
    val content: String,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?
)
package com.waiyanphyo.betternews.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waiyanphyo.betternews.data.local.entities.ArticleEntity

@JsonClass(generateAdapter = true)
data class NetworkArticle(
    @Json(name = "author")
    val author: String?,
    @Json(name = "content")
    val content: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "publishedAt")
    val publishedAt: String,
    @Json(name = "source")
    val source: NetworkSource,
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "urlToImage")
    val urlToImage: String?
)

fun NetworkArticle.asEntity() = ArticleEntity(
    author = author,
    content = content,
    description = description.toString(),
    publishedAt = publishedAt,
    source = source.asExternalModel(),
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun List<NetworkArticle>.asEntities() = this.map { it.asEntity() }
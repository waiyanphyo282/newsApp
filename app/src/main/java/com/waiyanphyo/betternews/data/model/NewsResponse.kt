package com.waiyanphyo.betternews.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waiyanphyo.betternews.data.network.models.NetworkArticle

@JsonClass(generateAdapter = true)
data class NewsResponse(
    @Json(name = "articles")
    val articles: List<NetworkArticle>,
    @Json(name = "status")
    val status: String,
    @Json(name = "totalResults")
    val totalResults: Int
)
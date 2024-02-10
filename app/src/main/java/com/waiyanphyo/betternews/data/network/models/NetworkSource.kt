package com.waiyanphyo.betternews.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waiyanphyo.betternews.data.model.Source

@JsonClass(generateAdapter = true)
data class NetworkSource(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String
)

fun NetworkSource.asExternalModel() = Source(id, name)
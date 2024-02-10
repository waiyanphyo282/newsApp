package com.waiyanphyo.betternews.data.network

import com.waiyanphyo.betternews.data.model.NewsResponse
import com.waiyanphyo.betternews.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String = "bitcoin",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

}
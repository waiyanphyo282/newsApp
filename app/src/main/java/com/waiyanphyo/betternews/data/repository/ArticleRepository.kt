package com.waiyanphyo.betternews.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.waiyanphyo.betternews.data.datasource.ArticleRemoteMediator
import com.waiyanphyo.betternews.data.local.daos.ArticleDao
import com.waiyanphyo.betternews.data.local.entities.asExternalModel
import com.waiyanphyo.betternews.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleRemoteMediator: ArticleRemoteMediator,
    private val articleDao: ArticleDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getArticles(searchQuery: String): Flow<PagingData<Article>> {
        Timber.d("getArticles: $searchQuery")
        val pagingSourceFactory = { articleDao.getAllArticles() }
        return Pager(
            config = PagingConfig(pageSize = 50),
            remoteMediator = articleRemoteMediator.search(searchQuery),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { it.map { it.asExternalModel() } }
    }

    fun getArticleById(id: Int) = articleDao.getArticleById(id)
}
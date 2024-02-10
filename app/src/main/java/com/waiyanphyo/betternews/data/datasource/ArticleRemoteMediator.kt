package com.waiyanphyo.betternews.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.waiyanphyo.betternews.data.local.NewsDatabase
import com.waiyanphyo.betternews.data.local.entities.ArticleEntity
import com.waiyanphyo.betternews.data.local.entities.ArticleKeys
import com.waiyanphyo.betternews.data.model.Article
import com.waiyanphyo.betternews.data.network.NewsApi
import com.waiyanphyo.betternews.data.network.models.asEntities
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator @Inject constructor(
    private val db: NewsDatabase,
    private val api: NewsApi,
): RemoteMediator<Int, ArticleEntity>() {

    private var query: String = "bitcoin"

    fun search(searchQuery: String): ArticleRemoteMediator {
        Timber.d("Query $searchQuery")
        this.query = searchQuery
        return this
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        Timber.d("loadType: $loadType")
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        Timber.d("Page: $page")
        try {
            val response = api.searchNews(searchQuery = query, pageNumber = page)
            Timber.d("Response: $response")
            val articles = response.body()?.articles
            val endOfPaginationReached = articles?.isEmpty() ?: false
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.articleKeysDao().clearArticleKeys()
                    db.articleDao().clearArticles()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles?.map {
                    ArticleKeys(url = it.url, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { db.articleKeysDao().insertAll(it) }
                articles?.asEntities()?.let { db.articleDao().insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArticleEntity>
    ): ArticleKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { url ->
                db.articleKeysDao().articleKeysByUrl(url)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleEntity>): ArticleKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                // Get the remote keys of the first items retrieved
                db.articleKeysDao().articleKeysByUrl(article.url)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleEntity>): ArticleKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                // Get the remote keys of the last item retrieved
                db.articleKeysDao().articleKeysByUrl(article.url)
            }
    }
}
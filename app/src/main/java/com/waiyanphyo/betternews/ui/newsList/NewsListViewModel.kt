package com.waiyanphyo.betternews.ui.newsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waiyanphyo.betternews.data.model.Article
import com.waiyanphyo.betternews.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private var _articlesFlow = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articlesFlow: StateFlow<PagingData<Article>> get() = _articlesFlow.asStateFlow()


    private var currentSearchQuery=  MutableLiveData("bitcoin")

    init {
        currentSearchQuery.observeForever {query ->
            Timber.d("SearchQuery: $query")
            viewModelScope.launch {
                articleRepository.getArticles(query).cachedIn(viewModelScope).collectLatest {
                    _articlesFlow.emit(it)
                }
            }
        }
    }

    fun changeSearchQuery(value: String) {
        currentSearchQuery.postValue(value)
    }




}
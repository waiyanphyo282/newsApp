package com.waiyanphyo.betternews.ui.newsDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyanphyo.betternews.data.local.entities.asExternalModel
import com.waiyanphyo.betternews.data.model.Article
import com.waiyanphyo.betternews.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val repository: ArticleRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableLiveData<NewsDetailUiState>()
    val uiState: LiveData<NewsDetailUiState> get() = _uiState

    init {
        savedStateHandle.get<Int>("article_id")?.let {
            getArticle(it)
        }
    }

    private fun getArticle(id: Int) = viewModelScope.launch {
        _uiState.postValue(NewsDetailUiState.Loading)
        repository.getArticleById(id).collectLatest {
            Timber.d("ArticleEntity: $it")
            _uiState.postValue(NewsDetailUiState.Success(it.asExternalModel()))
        }
    }

}

sealed class NewsDetailUiState {
    data object Loading: NewsDetailUiState()
    data class Success(val article: Article): NewsDetailUiState()
    data class Error(val message: String): NewsDetailUiState()
}
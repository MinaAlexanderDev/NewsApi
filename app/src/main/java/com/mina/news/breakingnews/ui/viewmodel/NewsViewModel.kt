package com.mina.news.breakingnews.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    //breaking news
    private var currentQueryBreakingNews: String? = null
    private var currentResultBreakingNews: Flow<PagingData<Article>>? = null
    fun getBreakingNews(queryString: String): Flow<PagingData<Article>> {
        val lastResult = currentResultBreakingNews

        if (queryString == currentQueryBreakingNews && lastResult != null) {
            return lastResult
        }
        currentQueryBreakingNews = queryString
        val newResult: Flow<PagingData<Article>> = repository.getFlowBreakingNews(queryString)
            .map { pagingData -> pagingData.map { it } }

            .cachedIn(viewModelScope)
        currentResultBreakingNews = newResult
        return newResult
    }

    ///search news
    private var currentQuerySearch: String = "android"
    private var currentResultSearch: Flow<PagingData<Article>>? = null
    var query: String = "android"
    fun getSearchNews(queryString: String): Flow<PagingData<Article>> {

        val lastResult = currentResultSearch

        if (queryString == currentQuerySearch && lastResult != null) {

            return lastResult

        }
        currentQuerySearch = queryString


        val newSearchResult: Flow<PagingData<Article>> = repository.getFlowSearchNews(queryString)
            .map { pagingData ->
                pagingData.map {
                    it

                }
            }
            .cachedIn(viewModelScope)
        currentQuerySearch = queryString
        return newSearchResult
    }

    //Load data from local db
    val item = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30,
            enablePlaceholders = false
        )
    )
    {
        repository.getElementsLiveData()
    }.flow

    fun addArticleToFavorite(article: Article) = viewModelScope.launch {
        repository.addToFavorite(article)
    }

    fun deleteArticleFromFavorite(article: Article) = viewModelScope.launch {
        repository.deleteFromFavorite(article)
    }


}



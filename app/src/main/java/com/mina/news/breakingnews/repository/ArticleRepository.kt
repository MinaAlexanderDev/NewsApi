package com.mina.news.breakingnews.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.pagingsource.PagingSourceItems
import com.mina.news.breakingnews.pagingsource.PagingSourceItemsSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ArticleRepository @Inject constructor(private val api: ArticleImp) {

    suspend fun addToFavorite(article: Article) {
        api.addArticle(article)
    }

    suspend fun deleteFromFavorite(article: Article) {
        api.deleteArticle(article)
    }

    fun getFlowBreakingNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceItems(api, query) }
        ).flow
    }

    fun getFlowSearchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSourceItemsSearch(api, query) }
        ).flow
    }

    fun getElementsLiveData(): PagingSource<Int, Article> {
        return api.getArticles()
    }
}
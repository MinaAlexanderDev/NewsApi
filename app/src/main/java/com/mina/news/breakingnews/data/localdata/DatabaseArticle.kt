package com.mina.news.breakingnews.data.localdata

import androidx.paging.PagingSource
import com.mina.news.breakingnews.models.repository.Article


interface DatabaseArticle {
    suspend fun addArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    fun getArticles(): PagingSource<Int, Article>
    suspend fun updateArticle(article: Article)
    suspend fun searchArticle(articleId: Int): List<Article>
}
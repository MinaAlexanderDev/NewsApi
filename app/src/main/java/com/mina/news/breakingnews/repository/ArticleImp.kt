package com.mina.news.breakingnews.repository

import androidx.paging.PagingSource
import com.mina.news.breakingnews.data.localdata.DatabaseArticle
import com.mina.news.breakingnews.data.remotedata.RemoteArticle
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.models.repository.Articles
import retrofit2.Response
import javax.inject.Inject

class ArticleImp @Inject constructor(
    private val remoteArticle: RemoteArticle,
    private val databaseArticle: DatabaseArticle,
) : com.mina.news.breakingnews.repository.Article {

    override suspend fun getBreakingNews(
        source: String,
        page: Int,
        perPage: Int,
        apikey: String
    ) = remoteArticle.getRepository(source, page, perPage, apikey)

    override suspend fun getSearchNews(
        query: String,
        page: Int,
        perPage: Int,
        apiKey: String
    ): Response<Articles> = remoteArticle.getSearchRepository(query, page, perPage, apiKey)

    override suspend fun addArticle(article: Article) = databaseArticle.addArticle(article)

    override suspend fun deleteArticle(article: Article) = databaseArticle.deleteArticle(article)

    override fun getArticles(): PagingSource<Int, Article> = databaseArticle.getArticles()

    override suspend fun updateArticle(article: Article) = databaseArticle.updateArticle(article)

    override suspend fun searchArticle(articledbId: Int) =
        databaseArticle.searchArticle(articledbId)

}
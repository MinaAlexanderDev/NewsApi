package com.mina.news.breakingnews.data.remotedata

import com.mina.news.breakingnews.api.ServiceAPI
import com.mina.news.breakingnews.models.repository.Articles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RemoteArticleImp @Inject constructor(private val api: ServiceAPI) : RemoteArticle {

    override suspend fun getRepository(
        query: String,
        page: Int,
        perPage: Int, apiKey: String
    ): Response<Articles> = withContext(Dispatchers.IO) {
        api.getBreakingNews(query, page, perPage, apiKey)
    }

    override suspend fun getSearchRepository(
        query: String,
        page: Int,
        perPage: Int,
        apiKey: String
    ): Response<Articles> = withContext(Dispatchers.IO) {
        api.getSearchNews(query, page, perPage, apiKey)
    }
}
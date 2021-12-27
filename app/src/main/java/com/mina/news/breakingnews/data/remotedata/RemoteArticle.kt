package com.mina.news.breakingnews.data.remotedata

import com.mina.news.breakingnews.models.repository.Articles
import retrofit2.Response

interface RemoteArticle {

    suspend fun getRepository(
        query: String, page: Int,
        perPage: Int, apiKey: String
    ): Response<Articles>

    suspend fun getSearchRepository(
        query: String, page: Int,
        perPage: Int, apiKey: String
    ): Response<Articles>
}
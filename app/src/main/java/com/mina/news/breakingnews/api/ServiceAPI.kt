package com.mina.news.breakingnews.api

import com.mina.news.breakingnews.models.repository.Articles
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceAPI {

    companion object {
        const val BASE_URL = "https://newsapi.org"
    }

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") source: String, @Query("page") page: Int,
        @Query("per_page") perPage: Int, @Query("apiKey") apiKey: String
    ): Response<Articles>

    @GET("/v2/everything")
    suspend fun getSearchNews(
        @Query("q") query: String, @Query("page") page: Int,
        @Query("per_page") perPage: Int, @Query("apiKey") apiKey: String
    ): Response<Articles>


}
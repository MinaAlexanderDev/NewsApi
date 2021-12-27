package com.mina.news.breakingnews.pagingsource

import androidx.paging.PagingSource
import com.mina.news.breakingnews.general.GeneralQuery
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.repository.ArticleImp
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class PagingSourceItemsSearch(private val api: ArticleImp, private val query: String) :
    PagingSource<Int, Article>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = api.getSearchNews(
                query,
                position,
                params.loadSize,
                GeneralQuery.apikey
            )
            val responseData = mutableListOf<Article>()
            val data = response.body()?.articles
            data?.let { responseData.addAll((data)) }
            if (data != null) {
                responseData.addAll((data))
            }
            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = if (data?.isEmpty() == true) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
package com.mina.news.breakingnews.data.localdata

import androidx.paging.PagingSource
import androidx.room.*
import com.mina.news.breakingnews.models.repository.Article


@Dao
interface ArticleDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(article: Article?): Long

    @Query("SELECT * FROM article_table ORDER BY id")
    fun getArticles(): PagingSource<Int, Article>

    @Delete
    suspend fun deleteUser(article: Article)

    @Update
    fun updateUser(article: Article?)

    @Query("SELECT * FROM article_table WHERE id =:articleId")
    fun searchUser(articleId: Int): List<Article>


}
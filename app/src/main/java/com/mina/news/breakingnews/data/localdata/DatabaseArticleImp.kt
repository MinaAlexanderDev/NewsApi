package com.mina.news.breakingnews.data.localdata

import com.mina.news.breakingnews.models.repository.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DatabaseArticleImp @Inject constructor(private val db: ArticlesDatabase) : DatabaseArticle {
    override suspend fun addArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.userDao().insertOrUpdateUser(article)
        }
    }

    override suspend fun deleteArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.userDao().deleteUser(article)
        }
    }

    override fun getArticles() = db.userDao().getArticles()
    override suspend fun updateArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.userDao().updateUser(article)
        }
    }

    override suspend fun searchArticle(article: Int) = withContext(Dispatchers.IO) {
        db.userDao().searchUser(article)
    }

}
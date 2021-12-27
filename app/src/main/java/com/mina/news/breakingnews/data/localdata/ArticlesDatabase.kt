package com.mina.news.breakingnews.data.localdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mina.news.breakingnews.models.repository.Article
import com.mina.news.breakingnews.models.repository.Converters

@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun userDao(): ArticleDAO

}
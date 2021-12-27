package com.mina.news.breakingnews.di

import android.content.Context
import androidx.room.Room
import com.mina.news.breakingnews.api.ServiceAPI
import com.mina.news.breakingnews.data.localdata.ArticleDAO
import com.mina.news.breakingnews.data.localdata.ArticlesDatabase
import com.mina.news.breakingnews.data.localdata.DatabaseArticle
import com.mina.news.breakingnews.data.localdata.DatabaseArticleImp
import com.mina.news.breakingnews.data.remotedata.RemoteArticle
import com.mina.news.breakingnews.data.remotedata.RemoteArticleImp
import com.mina.news.breakingnews.repository.Article
import com.mina.news.breakingnews.repository.ArticleImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ServiceAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): ServiceAPI =
        retrofit.create(ServiceAPI::class.java)

    @Module
    @InstallIn(ActivityComponent::class)
    abstract class RemoteArticleImpModule {

        @Binds
        abstract fun remoteArticleImp(
            remoteArticleImp: RemoteArticleImp
        ): RemoteArticle
    }

    @Module
    @InstallIn(ActivityComponent::class)
    abstract class ArticleImpModule {

        @Binds
        abstract fun articleImp(
            ArticleImp: ArticleImp
        ): Article
    }

    @Module
    @InstallIn(ActivityComponent::class)
    abstract class DatabaseArticleImpModule {

        @Binds
        abstract fun databaseArticleImp(
            DatabaseArticleImp: DatabaseArticleImp
        ): DatabaseArticle
    }

    private const val DATABASE_NAME = "user_database"

    @InstallIn(SingletonComponent::class)
    @Module
    class DatabaseModule {
        @Provides
        fun provideChannelDao(appDatabase: ArticlesDatabase): ArticleDAO {
            return appDatabase.userDao()
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ArticlesDatabase {
        return Room.databaseBuilder(
            appContext,
            ArticlesDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}
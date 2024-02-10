package com.waiyanphyo.betternews.di

import android.content.Context
import androidx.room.Room
import com.waiyanphyo.betternews.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            "better_news"
        ).build()
    }

    @Provides
    fun provideArticleDao(database: NewsDatabase) = database.articleDao()
}
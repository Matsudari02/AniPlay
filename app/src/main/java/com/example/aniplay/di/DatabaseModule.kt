package com.example.aniplay.di

import android.content.Context
import androidx.room.Room
import com.example.aniplay.data.local.AniPlayDatabase
import com.example.aniplay.data.local.FavoriteAnimeDao
import com.example.aniplay.data.local.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt responsável pela injeção de dependências do banco de dados Room e seus DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AniPlayDatabase =
        Room.databaseBuilder(context, AniPlayDatabase::class.java, "aniplay.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteAnimeDao(database: AniPlayDatabase): FavoriteAnimeDao =
        database.favoriteAnimeDao()

    @Provides
    fun provideUserProfileDao(database: AniPlayDatabase): UserProfileDao =
        database.userProfileDao()
}

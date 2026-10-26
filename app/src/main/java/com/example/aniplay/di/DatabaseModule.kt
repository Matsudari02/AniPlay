package com.example.aniplay.di

import android.content.Context
import androidx.room.Room
import com.example.aniplay.data.local.AniPlayDatabase
import com.example.aniplay.data.local.FavoriteAnimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Ensina o Hilt a criar o banco e o DAO.
 * O AnimeRepository recebe o DAO pelo construtor (@Inject) e o ViewModel recebe o repositório.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AniPlayDatabase =
        Room.databaseBuilder(context, AniPlayDatabase::class.java, "aniplay.db").build()

    @Provides
    fun provideFavoriteAnimeDao(database: AniPlayDatabase): FavoriteAnimeDao =
        database.favoriteAnimeDao()
}

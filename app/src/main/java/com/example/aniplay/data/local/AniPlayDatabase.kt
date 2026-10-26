package com.example.aniplay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteAnimeEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AniPlayDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
}

package com.example.aniplay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteAnimeEntity::class,
        UserProfileEntity::class,
        ProfileNoteEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AniPlayDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
    abstract fun userProfileDao(): UserProfileDao
}

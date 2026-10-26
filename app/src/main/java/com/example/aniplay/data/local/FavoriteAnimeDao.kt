package com.example.aniplay.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimeDao {

    // READ — a lista se atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM favorite_animes ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteAnimeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_animes WHERE animeId = :animeId)")
    fun observeIsFavorite(animeId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_animes WHERE animeId = :animeId)")
    suspend fun isFavorite(animeId: Int): Boolean

    // CREATE / UPDATE — REPLACE funciona como "upsert"
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteAnimeEntity)

    // DELETE
    @Query("DELETE FROM favorite_animes WHERE animeId = :animeId")
    suspend fun deleteById(animeId: Int)

    @Query("DELETE FROM favorite_animes")
    suspend fun deleteAll()
}

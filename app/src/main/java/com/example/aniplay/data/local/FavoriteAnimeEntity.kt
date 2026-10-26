package com.example.aniplay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aniplay.model.Anime

/** Tabela dos animes favoritados (usada pela tela "Minha Lista"). */
@Entity(tableName = "favorite_animes")
data class FavoriteAnimeEntity(
    @PrimaryKey val animeId: Int,
    val title: String,
    val imageUrl: String,
    val rating: Double,
    val addedAt: Long = System.currentTimeMillis(),
)

fun Anime.toFavoriteEntity() = FavoriteAnimeEntity(
    animeId = id,
    title = title,
    imageUrl = imageUrl,
    rating = rating,
)

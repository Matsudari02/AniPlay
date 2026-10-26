package com.example.aniplay.data.repository

import com.example.aniplay.data.local.FavoriteAnimeDao
import com.example.aniplay.data.local.FavoriteAnimeEntity
import com.example.aniplay.data.local.toFavoriteEntity
import com.example.aniplay.data.mock.MockAnimeData
import com.example.aniplay.model.Anime
import com.example.aniplay.model.HomeFeed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Única fonte de dados que os ViewModels enxergam.
 * - Catálogo: dados mock (simulam uma API com um pequeno atraso).
 * - Favoritos: Room, via [FavoriteAnimeDao] (injetado pelo Hilt).
 */
@Singleton
class AnimeRepository @Inject constructor(
    private val favoriteDao: FavoriteAnimeDao,
) {

    // ---------------- Catálogo (Home / Detalhes) ----------------

    suspend fun getHomeFeed(): HomeFeed {
        delay(NETWORK_DELAY_MS)
        val all = MockAnimeData.animes
        return HomeFeed(
            featured = all.take(5),
            trending = all.sortedByDescending { it.rating },
            newReleases = all.sortedByDescending { it.year },
            recommended = all.reversed(),
        )
    }

    /** Usado pela tela de Detalhes. */
    suspend fun getAnimeById(id: Int): Anime? {
        delay(NETWORK_DELAY_MS)
        return MockAnimeData.animes.firstOrNull { it.id == id }
    }

    // ---------------- Favoritos (Room) ----------------

    fun observeFavorites(): Flow<List<FavoriteAnimeEntity>> = favoriteDao.observeAll()

    fun observeIsFavorite(animeId: Int): Flow<Boolean> = favoriteDao.observeIsFavorite(animeId)

    suspend fun addFavorite(anime: Anime) = favoriteDao.insert(anime.toFavoriteEntity())

    suspend fun removeFavorite(animeId: Int) = favoriteDao.deleteById(animeId)

    suspend fun toggleFavorite(anime: Anime) {
        if (favoriteDao.isFavorite(anime.id)) removeFavorite(anime.id) else addFavorite(anime)
    }

    suspend fun clearFavorites() = favoriteDao.deleteAll()

    private companion object {
        const val NETWORK_DELAY_MS = 600L
    }
}

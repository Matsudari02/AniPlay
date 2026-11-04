package com.example.aniplay.model

/**
 * Modelo de domínio para episódios de um anime (INTEGRANTE 3).
 */
data class Episode(
    val id: Int,
    val animeId: Int,
    val number: Int,
    val title: String,
    val description: String,
    val durationSeconds: Int = 1420, // Exemplo: 23min 40s
    val thumbnailUrl: String,
    val videoUrl: String,
)

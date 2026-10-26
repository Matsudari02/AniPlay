package com.example.aniplay.model

/**
 * Modelo de domínio compartilhado por TODAS as telas.
 * Se precisar de campos novos (ex.: Episode), crie outro arquivo em `model/`
 * em vez de alterar este, para evitar conflitos de merge.
 */
data class Anime(
    val id: Int,
    val title: String,
    val synopsis: String,
    val imageUrl: String,   // capa vertical (2:3)
    val bannerUrl: String,  // banner horizontal (16:9)
    val genres: List<String>,
    val rating: Double,
    val episodes: Int,
    val year: Int,
    val studio: String,
)

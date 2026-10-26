package com.example.aniplay.model

/** Tudo o que a tela Home precisa para ser desenhada. */
data class HomeFeed(
    val featured: List<Anime>,
    val trending: List<Anime>,
    val newReleases: List<Anime>,
    val recommended: List<Anime>,
) {
    fun allGenres(): List<String> =
        (featured + trending + newReleases + recommended)
            .flatMap { it.genres }
            .distinct()
            .sorted()
}

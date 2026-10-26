package com.example.aniplay.ui.favorites

import androidx.compose.runtime.Composable
import com.example.aniplay.ui.components.PlaceholderScreen

/**
 * TELA 4 — Minha Lista / Favoritos (INTEGRANTE 4).
 * Crie `viewmodel/FavoritesViewModel.kt` e use repository.observeFavorites() (Room).
 * Mantenha a assinatura.
 */
@Composable
fun FavoritesScreen(
    onAnimeClick: (animeId: Int) -> Unit,
) {
    PlaceholderScreen(
        title = "Minha Lista",
        description = "Em construção (Room)",
    )
}

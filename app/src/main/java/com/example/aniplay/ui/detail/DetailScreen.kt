package com.example.aniplay.ui.detail

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.aniplay.ui.components.PlaceholderScreen

/**
 * TELA 2 — Detalhes do Anime (INTEGRANTE 2).
 * Mantenha esta assinatura: o NavHost já chama a tela assim.
 * Dica: crie `viewmodel/DetailViewModel.kt` com @HiltViewModel; o `animeId` chega via SavedStateHandle
 * (ou simplesmente como parâmetro, como aqui) e use repository.getAnimeById / toggleFavorite.
 */
@Composable
fun DetailScreen(
    animeId: Int,
    onBackClick: () -> Unit,
    onPlayEpisode: (animeId: Int, episode: Int) -> Unit,
) {
    PlaceholderScreen(
        title = "Detalhes do Anime",
        description = "Recebi o animeId = $animeId",
        onBackClick = onBackClick,
    ) {
        Button(onClick = { onPlayEpisode(animeId, 1) }) { Text("Assistir episódio 1") }
    }
}

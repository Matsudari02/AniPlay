package com.example.aniplay.ui.player

import androidx.compose.runtime.Composable
import com.example.aniplay.ui.components.PlaceholderScreen

/** TELA 3 — Player de Vídeo / Episódio (INTEGRANTE 3). Mantenha a assinatura. */
@Composable
fun PlayerScreen(
    animeId: Int,
    episodeNumber: Int,
    onBackClick: () -> Unit,
) {
    PlaceholderScreen(
        title = "Player",
        description = "Anime $animeId — episódio $episodeNumber",
        onBackClick = onBackClick,
    )
}

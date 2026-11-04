
package com.example.aniplay.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.aniplay.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    animeId: Int,
    onBackClick: () -> Unit,
    onPlayEpisode: (animeId: Int, episode: Int) -> Unit,
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val anime by viewModel.anime.collectAsStateWithLifecycle()

    LaunchedEffect(animeId) {
        viewModel.loadAnime(animeId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Voltar")
        }

        if (anime == null) {
            Text("Carregando anime...")
        } else {
            val atual = anime!!

            AsyncImage(
                model = atual.bannerUrl,
                contentDescription = atual.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = atual.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Text("⭐ ${atual.rating}  •  ${atual.year}")
            Text("Estúdio: ${atual.studio}")
            Text("Gêneros: ${atual.genres.joinToString(", ")}")
            Text("Episódios: ${atual.episodes}")

            Spacer(Modifier.height(16.dp))

            Text(
                "Sinopse",
                style = MaterialTheme.typography.titleLarge
            )
            Text(atual.synopsis)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onPlayEpisode(animeId, 1) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Assistir episódio 1")
            }

            OutlinedButton(
                onClick = { viewModel.toggleFavorite() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar / remover dos favoritos")
            }
        }
    }
}

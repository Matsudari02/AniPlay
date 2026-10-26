package com.example.aniplay.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aniplay.data.mock.MockAnimeData
import com.example.aniplay.ui.theme.AniPlayTheme
import com.example.aniplay.viewmodel.HomeSection
import com.example.aniplay.viewmodel.HomeUiState
import com.example.aniplay.viewmodel.HomeViewModel

/**
 * TELA 1 — Home / Catálogo (versão "stateful": conecta o ViewModel ao conteúdo).
 *
 * @param onAnimeClick recebe o ID do anime tocado; quem navega é o NavHost.
 */
@Composable
fun HomeScreen(
    onAnimeClick: (animeId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onAnimeClick = onAnimeClick,
        onGenreSelected = viewModel::onGenreSelected,
        onRetry = viewModel::loadHome,
        modifier = modifier,
    )
}

/** Versão "stateless": só recebe estado e eventos, por isso é fácil de testar e de pré-visualizar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onAnimeClick: (Int) -> Unit,
    onGenreSelected: (String?) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = uiState.errorMessage

    Scaffold(
        modifier = modifier,
        // A NavigationBar do Scaffold raiz já cobre a área inferior do sistema
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AniPlay",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: tela de busca */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when {
            uiState.isLoading -> LoadingState(contentModifier)
            errorMessage != null -> ErrorState(errorMessage, onRetry, contentModifier)
            else -> HomeLoadedContent(uiState, onAnimeClick, onGenreSelected, contentModifier)
        }
    }
}

@Composable
private fun HomeLoadedContent(
    uiState: HomeUiState,
    onAnimeClick: (Int) -> Unit,
    onGenreSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (uiState.featured.isNotEmpty()) {
            item(key = "featured") {
                FeaturedCarousel(animes = uiState.featured, onAnimeClick = onAnimeClick)
            }
        }

        item(key = "genres") {
            GenreChips(
                genres = uiState.genres,
                selectedGenre = uiState.selectedGenre,
                onGenreSelected = onGenreSelected,
            )
        }

        if (uiState.sections.isEmpty()) {
            item(key = "empty") {
                Text(
                    text = "Nenhum anime encontrado neste gênero.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        items(uiState.sections, key = { it.title }) { section ->
            AnimeSectionRow(
                title = section.title,
                animes = section.animes,
                onAnimeClick = onAnimeClick,
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) { Text("Tentar novamente") }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B0F)
@Composable
private fun HomeContentPreview() {
    val animes = MockAnimeData.animes
    AniPlayTheme {
        HomeContent(
            uiState = HomeUiState(
                isLoading = false,
                featured = animes.take(3),
                genres = listOf("Ação", "Comédia", "Fantasia"),
                sections = listOf(HomeSection("Em alta", animes)),
            ),
            onAnimeClick = {},
            onGenreSelected = {},
            onRetry = {},
        )
    }
}

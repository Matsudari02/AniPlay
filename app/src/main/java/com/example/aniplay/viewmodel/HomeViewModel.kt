package com.example.aniplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplay.data.repository.AnimeRepository
import com.example.aniplay.model.Anime
import com.example.aniplay.model.HomeFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeSection(
    val title: String,
    val animes: List<Anime>,
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val featured: List<Anime> = emptyList(),
    val genres: List<String> = emptyList(),
    val selectedGenre: String? = null,
    val sections: List<HomeSection> = emptyList(),
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Guarda o feed completo para refiltrar por gênero sem nova "chamada de rede"
    private var feed: HomeFeed? = null

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val loaded = repository.getHomeFeed()
                feed = loaded
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        featured = loaded.featured,
                        genres = loaded.allGenres(),
                        sections = buildSections(loaded, state.selectedGenre),
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Não foi possível carregar o catálogo.")
                }
            }
        }
    }

    /** Clicar no chip já selecionado (ou em "Todos", que envia null) remove o filtro. */
    fun onGenreSelected(genre: String?) {
        val currentFeed = feed ?: return
        _uiState.update { state ->
            val newGenre = if (genre == state.selectedGenre) null else genre
            state.copy(
                selectedGenre = newGenre,
                sections = buildSections(currentFeed, newGenre),
            )
        }
    }

    private fun buildSections(feed: HomeFeed, genre: String?): List<HomeSection> {
        fun List<Anime>.byGenre() = if (genre == null) this else filter { genre in it.genres }

        return listOf(
            HomeSection("Em alta", feed.trending.byGenre()),
            HomeSection("Novos lançamentos", feed.newReleases.byGenre()),
            HomeSection("Recomendados para você", feed.recommended.byGenre()),
        ).filter { it.animes.isNotEmpty() }
    }
}

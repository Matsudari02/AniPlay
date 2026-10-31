package com.example.aniplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplay.data.local.FavoriteAnimeEntity
import com.example.aniplay.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOption(val label: String) {
    DATE_ADDED_DESC("Mais recentes"),
    DATE_ADDED_ASC("Mais antigos"),
    RATING_DESC("Melhor avaliação"),
    TITLE_ASC("Nome (A-Z)"),
}

data class FavoritesUiState(
    val favorites: List<FavoriteAnimeEntity> = emptyList(),
    val filteredFavorites: List<FavoriteAnimeEntity> = emptyList(),
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.DATE_ADDED_DESC,
    val isLoading: Boolean = true,
    val showClearAllDialog: Boolean = false,
    val userMessage: String? = null,
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.observeFavorites().collect { list ->
                _uiState.update { state ->
                    state.copy(
                        favorites = list,
                        filteredFavorites = applyFilterAndSort(list, state.searchQuery, state.sortOption),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredFavorites = applyFilterAndSort(state.favorites, query, state.sortOption),
            )
        }
    }

    fun onSortOptionSelected(sort: SortOption) {
        _uiState.update { state ->
            state.copy(
                sortOption = sort,
                filteredFavorites = applyFilterAndSort(state.favorites, state.searchQuery, sort),
            )
        }
    }

    fun removeFavorite(animeId: Int, title: String) {
        viewModelScope.launch {
            repository.removeFavorite(animeId)
            _uiState.update {
                it.copy(userMessage = "\"$title\" removido da sua lista.")
            }
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            repository.clearFavorites()
            _uiState.update {
                it.copy(
                    showClearAllDialog = false,
                    userMessage = "Sua lista de favoritos foi limpa.",
                )
            }
        }
    }

    fun openClearAllDialog() {
        _uiState.update { it.copy(showClearAllDialog = true) }
    }

    fun closeClearAllDialog() {
        _uiState.update { it.copy(showClearAllDialog = false) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun applyFilterAndSort(
        list: List<FavoriteAnimeEntity>,
        query: String,
        sort: SortOption,
    ): List<FavoriteAnimeEntity> {
        val filtered = if (query.isBlank()) {
            list
        } else {
            list.filter { it.title.contains(query, ignoreCase = true) }
        }

        return when (sort) {
            SortOption.DATE_ADDED_DESC -> filtered.sortedByDescending { it.addedAt }
            SortOption.DATE_ADDED_ASC -> filtered.sortedBy { it.addedAt }
            SortOption.RATING_DESC -> filtered.sortedByDescending { it.rating }
            SortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
        }
    }
}

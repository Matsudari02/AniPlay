
package com.example.aniplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplay.data.repository.AnimeRepository
import com.example.aniplay.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _anime = MutableStateFlow<Anime?>(null)
    val anime = _anime.asStateFlow()

    fun loadAnime(animeId: Int) {
        viewModelScope.launch {
            _anime.value = repository.getAnimeById(animeId)
        }
    }

    fun toggleFavorite() {
        val animeAtual = _anime.value ?: return

        viewModelScope.launch {
            repository.toggleFavorite(animeAtual)
        }
    }
}

package com.example.aniplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplay.data.repository.AnimeRepository
import com.example.aniplay.model.Anime
import com.example.aniplay.model.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val isLoading: Boolean = true,
    val anime: Anime? = null,
    val episodes: List<Episode> = emptyList(),
    val currentEpisode: Episode? = null,
    val isPlaying: Boolean = true,
    val currentPositionSeconds: Float = 0f,
    val totalDurationSeconds: Float = 1420f, // 23min 40s por padrão
    val isMuted: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val audioTrack: String = "Japonês (Original)",
    val subtitleTrack: String = "Português (Brasil)",
    val quality: String = "1080p (FHD)",
    val showControls: Boolean = true,
    val isLockControls: Boolean = false,
    val isFullScreen: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val showEpisodesSheet: Boolean = false,
    val isFavorite: Boolean = false,
    val isLiked: Boolean = false,
    val userMessage: String? = null,
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var playbackJob: Job? = null

    fun loadPlayer(animeId: Int, episodeNumber: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val anime = repository.getAnimeById(animeId)
            val episodes = repository.getEpisodesForAnime(animeId)
            val episode = repository.getEpisode(animeId, episodeNumber)

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    anime = anime,
                    episodes = episodes,
                    currentEpisode = episode,
                    totalDurationSeconds = episode?.durationSeconds?.toFloat() ?: 1420f,
                    currentPositionSeconds = 0f,
                    isPlaying = true,
                )
            }

            // Observa se é favorito no Room
            anime?.let { a ->
                repository.observeIsFavorite(a.id).collect { isFav ->
                    _uiState.update { it.copy(isFavorite = isFav) }
                }
            }
        }

        startPlaybackTimer()
    }

    private fun startPlaybackTimer() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val state = _uiState.value
                if (state.isPlaying && !state.isLoading && (state.currentEpisode != null)) {
                    val nextPos = state.currentPositionSeconds + (1f * state.playbackSpeed)
                    if (nextPos >= state.totalDurationSeconds) {
                        _uiState.update { it.copy(currentPositionSeconds = state.totalDurationSeconds, isPlaying = false) }
                    } else {
                        _uiState.update { it.copy(currentPositionSeconds = nextPos) }
                    }
                }
            }
        }
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun seekTo(positionSeconds: Float) {
        val total = _uiState.value.totalDurationSeconds
        val clamped = positionSeconds.coerceIn(0f, total)
        _uiState.update { it.copy(currentPositionSeconds = clamped) }
    }

    fun rewind10s() {
        val current = _uiState.value.currentPositionSeconds
        seekTo(current - 10f)
    }

    fun forward10s() {
        val current = _uiState.value.currentPositionSeconds
        seekTo(current + 10f)
    }

    fun skipIntro() {
        // Pula os 85s da abertura do anime
        val current = _uiState.value.currentPositionSeconds
        seekTo(current + 85f)
        _uiState.update { it.copy(userMessage = "Abertura pulada (+1m 25s)") }
    }

    fun selectEpisode(episodeNumber: Int) {
        val animeId = _uiState.value.anime?.id ?: return
        viewModelScope.launch {
            val ep = repository.getEpisode(animeId, episodeNumber)
            if (ep != null) {
                _uiState.update {
                    it.copy(
                        currentEpisode = ep,
                        currentPositionSeconds = 0f,
                        totalDurationSeconds = ep.durationSeconds.toFloat(),
                        isPlaying = true,
                        showEpisodesSheet = false,
                        userMessage = "Trocado para ${ep.title}",
                    )
                }
            }
        }
    }

    fun nextEpisode() {
        val currentNum = _uiState.value.currentEpisode?.number ?: return
        val episodes = _uiState.value.episodes
        val next = episodes.firstOrNull { it.number == (currentNum + 1) }
        if (next != null) {
            selectEpisode(next.number)
        } else {
            _uiState.update { it.copy(userMessage = "Este é o último episódio disponível.") }
        }
    }

    fun previousEpisode() {
        val currentNum = _uiState.value.currentEpisode?.number ?: return
        val episodes = _uiState.value.episodes
        val prev = episodes.firstOrNull { it.number == (currentNum - 1) }
        if (prev != null) {
            selectEpisode(prev.number)
        } else {
            _uiState.update { it.copy(userMessage = "Este já é o primeiro episódio.") }
        }
    }

    fun toggleMute() {
        _uiState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun setPlaybackSpeed(speed: Float) {
        _uiState.update { it.copy(playbackSpeed = speed) }
    }

    fun setAudioTrack(audio: String) {
        _uiState.update { it.copy(audioTrack = audio, userMessage = "Áudio alterado para $audio") }
    }

    fun setSubtitleTrack(subtitle: String) {
        _uiState.update { it.copy(subtitleTrack = subtitle, userMessage = "Legenda alterada para $subtitle") }
    }

    fun setQuality(quality: String) {
        _uiState.update { it.copy(quality = quality, userMessage = "Qualidade alterada para $quality") }
    }

    fun toggleControls() {
        val current = _uiState.value.showControls
        _uiState.update { it.copy(showControls = !current) }
    }

    fun toggleLockControls() {
        val currentLock = _uiState.value.isLockControls
        _uiState.update {
            it.copy(
                isLockControls = !currentLock,
                showControls = true,
                userMessage = if (!currentLock) "Controles bloqueados" else "Controles desbloqueados",
            )
        }
    }

    fun toggleFullScreen() {
        _uiState.update { it.copy(isFullScreen = !it.isFullScreen) }
    }

    fun toggleFavorite() {
        val anime = _uiState.value.anime ?: return
        viewModelScope.launch {
            repository.toggleFavorite(anime)
        }
    }

    fun toggleLike() {
        _uiState.update {
            val nextLiked = !it.isLiked
            it.copy(
                isLiked = nextLiked,
                userMessage = if (nextLiked) "Você curtiu este episódio!" else null,
            )
        }
    }

    fun openSettings() {
        _uiState.update { it.copy(showSettingsDialog = true) }
    }

    fun closeSettings() {
        _uiState.update { it.copy(showSettingsDialog = false) }
    }

    fun openEpisodesSheet() {
        _uiState.update { it.copy(showEpisodesSheet = true) }
    }

    fun closeEpisodesSheet() {
        _uiState.update { it.copy(showEpisodesSheet = false) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
    }
}

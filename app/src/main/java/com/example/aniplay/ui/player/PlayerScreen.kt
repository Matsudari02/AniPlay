package com.example.aniplay.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.aniplay.ui.theme.AniOrange
import com.example.aniplay.viewmodel.PlayerViewModel

private fun createPlayerVectorIcon(name: String, pathDataSvg: String): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = PathParser().parsePathString(pathDataSvg).toNodes(),
        fill = SolidColor(Color.White),
    ).build()
}

private val IconPause = createPlayerVectorIcon("Pause", "M6 19h4V5H6v14zm8-14v14h4V5h-4z")
private val IconForward10 = createPlayerVectorIcon("Forward10", "M18 13c0 3.31-2.69 6-6 6s-6-2.69-6-6 2.69-6 6-6v4l5-5-5-5v4c-4.42 0-8 3.58-8 8s3.58 8 8 8 8-3.58 8-8h-2z")
private val IconReplay10 = createPlayerVectorIcon("Replay10", "M11.99 5V1l-5 5 5 5V7c3.31 0 6 2.69 6 6s-2.69 6-6 6-6-2.69-6-6h-2c0 4.42 3.58 8 8 8s8-3.58 8-8-3.58-8-8-8z")
private val IconSkipNext = createPlayerVectorIcon("SkipNext", "M6 18l8.5-6L6 6v12zM16 6v12h2V6h-2z")
private val IconSkipPrevious = createPlayerVectorIcon("SkipPrevious", "M6 6h2v12H6zm3.5 6l8.5 6V6z")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    animeId: Int,
    episodeNumber: Int,
    onBackClick: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(animeId, episodeNumber) {
        viewModel.loadPlayer(animeId, episodeNumber)
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!uiState.isFullScreen && uiState.showControls) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = uiState.anime?.title ?: "Carregando...",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "Episódio ${uiState.currentEpisode?.number ?: "-"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favoritar",
                                tint = if (uiState.isFavorite) AniOrange else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        IconButton(onClick = { viewModel.openSettings() }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Configurações")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                )
            }
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AniOrange)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (uiState.isFullScreen) PaddingValues(0.dp) else innerPadding)
                    .background(Color.Black),
            ) {
                // ÁREA DO PLAYER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { viewModel.toggleControls() },
                    contentAlignment = Alignment.Center,
                ) {
                    // Simulação de Vídeo (Imagem do Episódio)
                    AsyncImage(
                        model = uiState.currentEpisode?.thumbnailUrl ?: uiState.anime?.imageUrl,
                        contentDescription = "Player",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        alpha = if (uiState.showControls) 0.5f else 1.0f,
                    )

                    // Overlay de Controles
                    if (uiState.showControls) {
                        PlayerControlsOverlay(
                            isPlaying = uiState.isPlaying,
                            isLocked = uiState.isLockControls,
                            onPlayPause = { viewModel.togglePlayPause() },
                            onRewind = { viewModel.rewind10s() },
                            onForward = { viewModel.forward10s() },
                            onSkipNext = { viewModel.nextEpisode() },
                            onSkipPrevious = { viewModel.previousEpisode() },
                            onLock = { viewModel.toggleLockControls() },
                        )
                        
                        // Botão Skip Intro
                        if (uiState.currentPositionSeconds < 85f && !uiState.isLockControls) {
                            Button(
                                onClick = { viewModel.skipIntro() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    contentColor = Color.White,
                                ),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 60.dp, end = 16.dp),
                            ) {
                                Text("Pular Abertura", fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        // Barra de Progresso e Tempo
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(formatTime(uiState.currentPositionSeconds), color = Color.White, style = MaterialTheme.typography.labelMedium)
                                Text(formatTime(uiState.totalDurationSeconds), color = Color.White, style = MaterialTheme.typography.labelMedium)
                            }
                            Slider(
                                value = uiState.currentPositionSeconds,
                                onValueChange = { viewModel.seekTo(it) },
                                valueRange = 0f..uiState.totalDurationSeconds,
                                colors = SliderDefaults.colors(
                                    thumbColor = AniOrange,
                                    activeTrackColor = AniOrange,
                                    inactiveTrackColor = Color.Gray,
                                ),
                                enabled = !uiState.isLockControls,
                            )
                        }
                    }
                }

                // INFORMAÇÕES ABAIXO DO VÍDEO (Apenas se não for FullScreen)
                if (!uiState.isFullScreen) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp),
                    ) {
                        Text(
                            text = uiState.currentEpisode?.title ?: "Episódio ${uiState.currentEpisode?.number}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Button(
                                onClick = { viewModel.toggleLike() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.isLiked) AniOrange else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (uiState.isLiked) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Filled.ThumbUp, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (uiState.isLiked) "Curtiu" else "Curtir")
                            }
                            Button(
                                onClick = { viewModel.openEpisodesSheet() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Filled.Settings, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Episódios")
                            }
                        }
                    }
                }
            }
        }
        
        // BOTTOM SHEET DE EPISÓDIOS
        if (uiState.showEpisodesSheet) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = { viewModel.closeEpisodesSheet() },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Episódios",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.episodes, key = { it.number }) { ep ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectEpisode(ep.number) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AsyncImage(
                                    model = ep.thumbnailUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp, 56.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.DarkGray),
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "${ep.number}. ${ep.title}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (ep.number == uiState.currentEpisode?.number) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (ep.number == uiState.currentEpisode?.number) AniOrange else MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        text = "${ep.durationSeconds / 60} min",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerControlsOverlay(
    isPlaying: Boolean,
    isLocked: Boolean,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onLock: () -> Unit,
) {
    if (isLocked) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            IconButton(
                onClick = onLock,
                modifier = Modifier
                    .padding(32.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50)),
            ) {
                Icon(Icons.Filled.Lock, contentDescription = "Desbloquear", tint = Color.White)
            }
        }
        return
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        IconButton(onClick = onSkipPrevious) {
            Icon(IconSkipPrevious, contentDescription = "Anterior", tint = Color.White, modifier = Modifier.size(36.dp))
        }
        IconButton(onClick = onRewind) {
            Icon(IconReplay10, contentDescription = "-10s", tint = Color.White, modifier = Modifier.size(42.dp))
        }
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .size(72.dp)
                .background(AniOrange.copy(alpha = 0.8f), RoundedCornerShape(50)),
        ) {
            Icon(
                imageVector = if (isPlaying) IconPause else Icons.Filled.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.Black,
                modifier = Modifier.size(48.dp),
            )
        }
        IconButton(onClick = onForward) {
            Icon(IconForward10, contentDescription = "+10s", tint = Color.White, modifier = Modifier.size(42.dp))
        }
        IconButton(onClick = onSkipNext) {
            Icon(IconSkipNext, contentDescription = "Próximo", tint = Color.White, modifier = Modifier.size(36.dp))
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
        IconButton(
            onClick = onLock,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50)),
        ) {
            Icon(Icons.Filled.Lock, contentDescription = "Bloquear", tint = Color.White)
        }
    }
}

private fun formatTime(seconds: Float): String {
    val mins = (seconds.toInt() / 60).toString().padStart(2, '0')
    val secs = (seconds.toInt() % 60).toString().padStart(2, '0')
    return "$mins:$secs"
}

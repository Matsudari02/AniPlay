package com.example.aniplay.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/** Todas as rotas do app. Cada integrante só mexe na rota da própria tela. */
sealed class Screen(val route: String) {

    // --- Abas da barra inferior ---
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")

    // --- Telas com parâmetros ---
    data object Detail : Screen("detail/{animeId}") {
        const val ARG_ANIME_ID = "animeId"
        fun createRoute(animeId: Int) = "detail/$animeId"
    }

    data object Player : Screen("player/{animeId}/{episode}") {
        const val ARG_ANIME_ID = "animeId"
        const val ARG_EPISODE = "episode"
        fun createRoute(animeId: Int, episode: Int) = "player/$animeId/$episode"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Início", Icons.Filled.Home),
    BottomNavItem(Screen.Favorites, "Minha Lista", Icons.Filled.Favorite),
    BottomNavItem(Screen.Profile, "Perfil", Icons.Filled.Person),
)

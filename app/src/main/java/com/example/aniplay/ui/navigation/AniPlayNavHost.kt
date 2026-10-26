package com.example.aniplay.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aniplay.ui.detail.DetailScreen
import com.example.aniplay.ui.favorites.FavoritesScreen
import com.example.aniplay.ui.home.HomeScreen
import com.example.aniplay.ui.player.PlayerScreen
import com.example.aniplay.ui.profile.ProfileScreen

@Composable
fun AniPlayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        // Tela 1 — Home: envia o ID do anime clicado para a tela de Detalhes
        composable(Screen.Home.route) {
            HomeScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                },
            )
        }

        // Tela 2 — Detalhes: recebe animeId e envia (animeId, episódio) para o Player
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(Screen.Detail.ARG_ANIME_ID) { type = NavType.IntType }),
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(Screen.Detail.ARG_ANIME_ID) ?: return@composable
            DetailScreen(
                animeId = animeId,
                onBackClick = { navController.popBackStack() },
                onPlayEpisode = { id, episode ->
                    navController.navigate(Screen.Player.createRoute(id, episode))
                },
            )
        }

        // Tela 3 — Player: recebe animeId e número do episódio
        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument(Screen.Player.ARG_ANIME_ID) { type = NavType.IntType },
                navArgument(Screen.Player.ARG_EPISODE) { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(Screen.Player.ARG_ANIME_ID) ?: return@composable
            val episode = backStackEntry.arguments?.getInt(Screen.Player.ARG_EPISODE) ?: return@composable
            PlayerScreen(
                animeId = animeId,
                episodeNumber = episode,
                onBackClick = { navController.popBackStack() },
            )
        }

        // Tela 4 — Minha Lista (Room)
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                },
            )
        }

        // Tela 5 — Perfil e Configurações
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}

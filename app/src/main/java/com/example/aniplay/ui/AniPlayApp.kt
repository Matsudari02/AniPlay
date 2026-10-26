package com.example.aniplay.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aniplay.ui.navigation.AniPlayNavHost
import com.example.aniplay.ui.navigation.bottomNavItems

/**
 * Scaffold raiz: só controla a barra inferior e hospeda o NavHost.
 *
 * REGRA DE INSETS: este Scaffold usa contentWindowInsets zerado e cada tela cuida da
 * própria barra de status (o TopAppBar já faz isso). Telas das abas (Home, Minha Lista,
 * Perfil) devem zerar o inset do próprio Scaffold, pois a NavigationBar já ocupa a
 * área da barra de navegação do sistema.
 */
@Composable
fun AniPlayApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // A barra inferior só aparece nas 3 abas principais (some em Detalhes e Player)
    val showBottomBar = bottomNavItems.any { it.screen.route == currentDestination?.route }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        AniPlayNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

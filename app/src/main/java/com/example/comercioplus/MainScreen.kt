package com.example.comercioplus

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Define las pantallas de la app para la navegación
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : Screen("dashboard", "Inicio", Icons.Default.Home) // Cambiado a Inicio
    object ProductList : Screen("product_list", "Productos", Icons.Default.Store)
    object Categories : Screen("categories", "Categorías", Icons.Default.Category)
    object Analytics : Screen("analytics", "Analítica", Icons.Default.Analytics)
    object Settings : Screen("settings", "Configuración", Icons.Default.Settings)
}

// Lista de ítems para la barra de navegación inferior
val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.ProductList,
    Screen.Categories,
    Screen.Analytics,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    title: String,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = floatingActionButton
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

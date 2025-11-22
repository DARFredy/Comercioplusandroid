package com.example.comercioplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.comercioplus.ui.theme.ComercioplusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Obtiene el ViewModel de configuración
            val settingsViewModel: SettingsViewModel = viewModel()
            val appearanceState by settingsViewModel.appearanceState.collectAsState()

            // Aplica el tema claro u oscuro según la preferencia guardada
            ComercioplusTheme(darkTheme = appearanceState.isDarkMode) {
                ComercioPlusNavHost()
            }
        }
    }
}

@Composable
fun ComercioPlusNavHost() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = "landing") { // Restaurado a "landing"
        composable("landing") { LandingScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }

        composable("dashboard") {
            MainScreen(navController = navController, title = "Inicio") { modifier ->
                DashboardScreen(navController = navController, modifier = modifier)
            }
        }
        composable("product_list") {
            MainScreen(
                navController = navController,
                title = "Tienda",
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate("add_product") },
                        containerColor = MaterialTheme.colorScheme.primary // Botón en color naranja
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir producto")
                    }
                }
            ) { modifier ->
                ProductListScreen(navController = navController, modifier = modifier)
            }
        }
        composable(
            route = "add_product?productId={productId}",
            arguments = listOf(navArgument("productId") { nullable = true; defaultValue = null })
        ) { backStackEntry ->
            AddProductScreen(
                navController = navController,
                viewModel = productViewModel,
                productId = backStackEntry.arguments?.getString("productId")
            )
        }
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            ProductDetailScreen(
                navController = navController,
                viewModel = productViewModel,
                productId = backStackEntry.arguments?.getString("productId") ?: ""
            )
        }
        composable("categories") {
            MainScreen(navController = navController, title = "Categorías") { modifier ->
                CategoriesScreen(navController = navController, modifier = modifier)
            }
        }
        composable("analytics") {
            MainScreen(navController = navController, title = "Analítica") { modifier ->
                AnalyticsScreen(modifier = modifier)
            }
        }
        composable("settings") {
            MainScreen(navController = navController, title = "Configuración") { modifier ->
                SettingsScreen(navController = navController, modifier = modifier)
            }
        }
    }
}
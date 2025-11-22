package com.example.comercioplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.comercioplus.model.Product
import com.example.comercioplus.ui.theme.ComercioplusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComercioplusTheme {
                ComercioPlusNavHost()
            }
        }
    }
}

@Composable
fun ComercioPlusNavHost() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val productViewModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }

        composable("dashboard") {
            MainScreen(navController = navController, currentRoute = currentRoute, title = "Dashboard") {
                DashboardScreen(navController = navController)
            }
        }
        composable("product_list") {
            MainScreen(
                navController = navController,
                currentRoute = currentRoute,
                title = "Productos",
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("add_product") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir producto")
                    }
                }
            ) { modifier ->
                ProductGrid(navController = navController, viewModel = productViewModel, modifier = modifier)
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
            MainScreen(navController = navController, currentRoute = currentRoute, title = "Categorías") {
                CategoriesScreen(navController = navController)
            }
        }
        composable("settings") {
            MainScreen(navController = navController, currentRoute = currentRoute, title = "Configuración") {
                SettingsScreen(navController = navController)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            if (product.imageUrl.isBlank()) {
                Icon(
                    imageVector = Icons.Filled.BrokenImage,
                    contentDescription = "No hay imagen",
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(16.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProductGrid(navController: NavController, viewModel: ProductViewModel, modifier: Modifier = Modifier) {
    val products by viewModel.products.collectAsState()
    var searchText by remember { mutableStateOf("") }

    if (products.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No hay productos todavía", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("add_product") }) {
                    Text("Crear nuevo producto")
                }
            }
        }
    } else {
        Column(modifier = modifier.padding(16.dp)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar productos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(products.filter { it.name.contains(searchText, ignoreCase = true) }) { product ->
                    ProductCard(product = product) {
                        navController.navigate("product_detail/${product.id}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A202C)
@Composable
fun ProductGridPreview() {
    ComercioplusTheme {
        val navController = rememberNavController()
        val productViewModel: ProductViewModel = viewModel()
        ProductGrid(navController = navController, viewModel = productViewModel)
    }
}

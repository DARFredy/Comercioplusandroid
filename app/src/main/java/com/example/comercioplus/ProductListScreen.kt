package com.example.comercioplus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow // Importación correcta
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.comercioplus.model.Product

@Composable
fun ProductListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera de la tienda
        item(span = { GridItemSpan(maxLineSpan) }) {
            StoreHeader(uiState)
        }

        // Buscador
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar productos...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
        
        // Mensaje si no hay productos
        if (uiState.products.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(50.dp)) {
                    Text("Aún no hay productos en la tienda.")
                }
            }
        } else {
             // Cuadrícula de productos
            items(uiState.products.filter { it.name.contains(searchText, ignoreCase = true) }) { product ->
                ProductCard(product = product) {
                    navController.navigate("product_detail/${product.id}")
                }
            }
        }
    }
}

@Composable
fun StoreHeader(uiState: ProductListUiState) {
    Box(modifier = Modifier.padding(bottom = 60.dp)) {
        // Imagen de portada
        Card(shape = MaterialTheme.shapes.large, modifier = Modifier.height(180.dp)) {
            if (uiState.coverUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uiState.coverUri),
                    contentDescription = "Portada de la tienda",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                     Icon(Icons.Default.Image, "Portada", modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Logo y nombre de la tienda
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier.size(100.dp).border(4.dp, MaterialTheme.colorScheme.background, CircleShape)
            ) {
                if (uiState.logoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.logoUri),
                        contentDescription = "Logo de la tienda",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                     Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Storefront, "Logo", modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Texto con sombra para mejorar la legibilidad
            Text(
                text = uiState.storeName, 
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // Siempre blanco para que contraste
                    shadow = Shadow(color = Color.Black, blurRadius = 8f)
                )
            )
            Text(
                text = "Explora el catálogo y encuentra lo que buscas. ${uiState.productCount} productos.", 
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    shadow = Shadow(color = Color.Black, blurRadius = 6f)
                )
            )
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            if (product.imageUrl.isBlank()) {
                Box(modifier = Modifier.height(120.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.BrokenImage, contentDescription = "No hay imagen", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl), // Corregido
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
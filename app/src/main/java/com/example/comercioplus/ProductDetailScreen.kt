package com.example.comercioplus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, viewModel: ProductViewModel, productId: String) {

    LaunchedEffect(productId) {
        viewModel.findProductById(productId)
    }

    val product by viewModel.selectedProduct.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedProduct()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            val currentProduct = product
            if (currentProduct == null) {
                CircularProgressIndicator()
            } else {
                // Contenedor principal para separar el contenido de los botones
                Column(modifier = Modifier.fillMaxSize()) {
                    // Contenido desplazable
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        if (currentProduct.imageUrl.isBlank()) {
                            Icon(
                                imageVector = Icons.Filled.BrokenImage,
                                contentDescription = "No hay imagen",
                                modifier = Modifier.fillMaxWidth().height(250.dp)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(currentProduct.imageUrl),
                                contentDescription = currentProduct.name,
                                modifier = Modifier.fillMaxWidth().height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(currentProduct.name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(currentProduct.description, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$${currentProduct.price}", style = MaterialTheme.typography.headlineSmall)
                    }

                    // Botones fijos en la parte inferior
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("add_product?productId=${currentProduct.id}") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar")
                        }
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Confirmar Eliminación") },
                        text = { Text("¿Estás seguro de que quieres eliminar este producto? Esta acción no se puede deshacer.") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteProduct(currentProduct.id)
                                    showDeleteDialog = false
                                    navController.popBackStack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Eliminar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}

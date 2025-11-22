package com.example.comercioplus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.comercioplus.ui.theme.ComercioplusTheme

@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Aumenta el espaciado vertical
    ) {
        // Encabezado de bienvenida
        WelcomeHeader(uiState = uiState)

        // Botones de acción rápida
        ActionButtons(navController = navController)

        // Sección de estadísticas
        Text("Resumen de la tienda", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardStat(title = "Productos", value = uiState.productCount.toString(), modifier = Modifier.weight(1f))
            DashboardStat(title = "Ventas (24h)", value = "0", modifier = Modifier.weight(1f))
            DashboardStat(title = "Visitas (24h)", value = "0", modifier = Modifier.weight(1f))
        }

        // Mensaje de "Aún no hay actividad"
        if (uiState.productCount == 0) {
            NoActivityCard { navController.navigate("add_product") }
        }
    }
}

@Composable
fun WelcomeHeader(uiState: DashboardUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(shape = CircleShape, modifier = Modifier.size(64.dp)) {
            if (uiState.logoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uiState.logoUri),
                    contentDescription = "Logo de la tienda",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Storefront, contentDescription = "Logo", modifier = Modifier.fillMaxSize().padding(12.dp))
            }
        }
        Column {
            Text(text = "¡Hola!", style = MaterialTheme.typography.bodyMedium)
            Text(text = uiState.storeName, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun ActionButtons(navController: NavController) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ActionButton(icon = Icons.Default.AddCircle, text = "Añadir producto", modifier = Modifier.weight(1f)) {
            navController.navigate("add_product")
        }
        ActionButton(icon = Icons.Default.Storefront, text = "Ver tienda", modifier = Modifier.weight(1f)) {
            navController.navigate("product_list")
        }
        ActionButton(icon = Icons.Default.Palette, text = "Personalizar", modifier = Modifier.weight(1f)) {
            navController.navigate("settings")
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.medium, contentPadding = PaddingValues(vertical = 20.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(28.dp))
            Text(text, fontSize = 12.sp)
        }
    }
}

@Composable
fun DashboardStat(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun NoActivityCard(onAddProductClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Aún no hay actividad para mostrar. \nAgrega productos o configura tu tienda para empezar.",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onAddProductClick) {
                Text("Agregar producto")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    ComercioplusTheme {
        DashboardScreen(rememberNavController())
    }
}

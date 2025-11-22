package com.example.comercioplus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    // Datos de ejemplo
    val categories = listOf("Calcomanía", "Accesorios", "Repuestos", "Llantas", "Frenos")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Barra de búsqueda y botón de añadir
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar categoría...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            Button(onClick = { /* TODO: Navegar a pantalla de crear categoría */ }) {
                Text("+ Nueva categoría")
            }
        }

        // Cabecera de la lista
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("Nombre", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("Slug", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("Productos", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("Acciones", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
        }
        Divider()

        // Lista de Categorías
        LazyColumn {
            items(categories.filter { it.contains(searchText, ignoreCase = true) }) { categoryName ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(categoryName, modifier = Modifier.weight(1f))
                    Text(categoryName.lowercase(), modifier = Modifier.weight(1f))
                    Text("0", modifier = Modifier.weight(0.5f))
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { /*TODO: Editar*/ }) { Text("Editar") }
                        TextButton(onClick = { /*TODO: Eliminar*/ }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Eliminar") }
                    }
                }
                Divider()
            }
        }
    }
}
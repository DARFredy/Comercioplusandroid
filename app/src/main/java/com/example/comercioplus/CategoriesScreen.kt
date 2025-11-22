package com.example.comercioplus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.comercioplus.model.Category

@Composable
fun CategoriesScreen(
    navController: NavController, // Mantengo NavController por si se necesita en el futuro
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no has creado ninguna categoría.")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onEdit = { categoryToEdit = category; showDialog = true },
                        onDelete = { viewModel.deleteCategory(category.id) }
                    )
                }
            }
        }

        // Botón flotante para añadir categoría
        FloatingActionButton(
            onClick = { categoryToEdit = null; showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary // Color naranja
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Añadir categoría")
        }
    }

    // Diálogo para crear/editar categoría
    if (showDialog) {
        CategoryDialog(
            category = categoryToEdit,
            onDismiss = { showDialog = false },
            onConfirm = {
                if (categoryToEdit == null) {
                    viewModel.addCategory(it.name)
                } else {
                    viewModel.updateCategory(it)
                }
            }
        )
    }
}

@Composable
fun CategoryCard(category: Category, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = category.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Slug: ${category.slug}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("${category.productCount} productos", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(category: Category?, onDismiss: () -> Unit, onConfirm: (Category) -> Unit) {
    var name by remember { mutableStateOf(category?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Nueva Categoría" else "Editar Categoría") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la categoría") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                val slug = name.lowercase().replace(" ", "-")
                onConfirm(category?.copy(name = name, slug = slug) ?: Category(name = name, slug = slug))
                onDismiss()
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

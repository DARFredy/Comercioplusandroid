package com.example.comercioplus

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, viewModel: ProductViewModel, productId: String?) {

    val isEditing = productId != null
    val productToEdit by viewModel.selectedProduct.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = productToEdit) {
        if (isEditing && productToEdit != null) {
            productName = productToEdit!!.name
            productDescription = productToEdit!!.description
            productPrice = productToEdit!!.price.toString()
            // No se puede precargar la imagen de la galería, solo la URL
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Producto" else "Añadir Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = productName, onValueChange = { productName = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = productDescription, onValueChange = { productDescription = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = productPrice, onValueChange = { productPrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)){
                 OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Elegir de la galería")
                }
                if (imageUri != null) {
                    Text("Imagen seleccionada")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val price = productPrice.toDoubleOrNull() ?: 0.0
                    val imageUrl = imageUri?.toString() ?: productToEdit?.imageUrl ?: ""

                    if (isEditing && productId != null) {
                        viewModel.updateProduct(productId, productName, productDescription, price, imageUrl)
                    } else {
                        viewModel.addProduct(productName, productDescription, price, imageUrl)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Guardar Producto")
            }
        }
    }
}

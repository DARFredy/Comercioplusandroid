package com.example.comercioplus

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.comercioplus.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    productId: String?,
    categoryViewModel: CategoryViewModel = viewModel() // ViewModel para las categorías
) {

    val isEditing = productId != null
    val productToEdit by viewModel.selectedProduct.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") } // Nuevo estado para la categoría
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    // Efecto para buscar el producto si estamos en modo edición
    LaunchedEffect(key1 = productId) {
        if (isEditing && productId != null) {
            viewModel.findProductById(productId)
        } else {
            viewModel.clearSelectedProduct() // Limpia el producto si no estamos editando
        }
    }

    // Efecto para rellenar el formulario cuando el producto a editar se ha cargado
    LaunchedEffect(key1 = productToEdit) {
        if (isEditing && productToEdit != null && productToEdit!!.id == productId) {
            productName = productToEdit!!.name
            productDescription = productToEdit!!.description
            productPrice = productToEdit!!.price.toString()
            productCategory = productToEdit!!.category // Cargar la categoría del producto
            // No sobreescribir la imagen si el usuario ya ha elegido una nueva
            if (imageUri == null) {
                 imageUri = productToEdit!!.imageUrl.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
            }
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Añadido para hacer la pantalla desplazable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado uniforme
        ) {
            OutlinedTextField(value = productName, onValueChange = { productName = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = productDescription, onValueChange = { productDescription = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            OutlinedTextField(value = productPrice, onValueChange = { productPrice = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())

            // Selector de Categoría
            ExposedDropdownMenuBox(
                expanded = isCategoryDropdownExpanded,
                onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = productCategory,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isCategoryDropdownExpanded,
                    onDismissRequest = { isCategoryDropdownExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                productCategory = category.name
                                isCategoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                 OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Elegir de la galería")
                }
                if (imageUri != null) {
                    Text("Imagen seleccionada")
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            Button(
                onClick = {
                    val price = productPrice.toDoubleOrNull() ?: 0.0
                    val finalImageUrl = imageUri?.toString() ?: ""

                    if (isEditing && productId != null) {
                        viewModel.updateProduct(productId, productName, productDescription, price, finalImageUrl, productCategory)
                    } else {
                        viewModel.addProduct(productName, productDescription, price, finalImageUrl, productCategory)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = productName.isNotBlank() && productPrice.isNotBlank() && productCategory.isNotBlank()
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Guardar Producto")
            }
        }
    }
}
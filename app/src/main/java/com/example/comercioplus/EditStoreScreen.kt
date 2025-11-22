package com.example.comercioplus

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStoreScreen(navController: NavController, settingsViewModel: SettingsViewModel = viewModel()) {
    val generalState by settingsViewModel.generalState.collectAsState()
    val appearanceState by settingsViewModel.appearanceState.collectAsState()
    var showSaveMessage by remember { mutableStateOf(false) }

    val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { settingsViewModel.onLogoUriChange(it) }
    val coverLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { settingsViewModel.onCoverUriChange(it) }

    if (showSaveMessage) {
        LaunchedEffect(key1 = Unit) {
            delay(2000)
            showSaveMessage = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tienda") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Sección para Nombre de la Tienda ---
            OutlinedTextField(
                value = generalState.storeName,
                onValueChange = settingsViewModel::onStoreNameChange,
                label = { Text("Nombre de la tienda") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Sección para Logo ---
            Text("Logo", style = MaterialTheme.typography.titleMedium)
            if (appearanceState.logoUri == null) {
                Box(modifier = Modifier.size(100.dp).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { Text("[Logo]") }
            } else {
                AsyncImage(model = appearanceState.logoUri, contentDescription = "Vista previa del Logo", modifier = Modifier.size(100.dp), contentScale = ContentScale.Crop)
            }
            Button(onClick = { logoLauncher.launch("image/*") }) { Text("Cambiar logo") }

            // --- Sección para Portada ---
            Text("Portada", style = MaterialTheme.typography.titleMedium)
            if (appearanceState.coverUri == null) {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { Text("[Portada]") }
            } else {
                AsyncImage(model = appearanceState.coverUri, contentDescription = "Vista previa de la Portada", modifier = Modifier.fillMaxWidth().height(150.dp), contentScale = ContentScale.Crop)
            }
            Button(onClick = { coverLauncher.launch("image/*") }) { Text("Cambiar portada") }

            // --- Botón de Guardar ---
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    settingsViewModel.saveGeneralSettings()
                    settingsViewModel.saveAppearanceSettings()
                    showSaveMessage = true
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar Cambios") }

            if (showSaveMessage) {
                Text(
                    text = "Tienda actualizada correctamente",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )
            }
        }
    }
}
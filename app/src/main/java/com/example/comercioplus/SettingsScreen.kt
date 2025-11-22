package com.example.comercioplus

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun SettingsScreen(
    navController: NavController, // Mantengo NavController por si se necesita en el futuro
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("General", "Apariencia", "Pagos", "Envíos", "Impuestos", "Notificaciones")

    // Recopila todos los estados del ViewModel
    val generalState by viewModel.generalState.collectAsState()
    val appearanceState by viewModel.appearanceState.collectAsState()
    val paymentsState by viewModel.paymentsState.collectAsState()
    val shippingState by viewModel.shippingState.collectAsState()
    val taxesState by viewModel.taxesState.collectAsState()
    val notificationsState by viewModel.notificationsState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) })
            }
        }

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)) {
            when (tabIndex) {
                0 -> GeneralSettingsSection(generalState, viewModel)
                1 -> AppearanceSettingsSection(appearanceState, viewModel)
                2 -> PaymentsSettingsSection(paymentsState, viewModel)
                3 -> ShippingSettingsSection(shippingState, viewModel)
                4 -> TaxesSettingsSection(taxesState, viewModel)
                5 -> NotificationsSettingsSection(notificationsState, viewModel)
            }
        }
    }
}

@Composable
private fun GeneralSettingsSection(state: GeneralSettingsState, viewModel: SettingsViewModel) {
    SettingsCard(title = "Configuración General", onSave = { viewModel.saveGeneralSettings() }) {
        OutlinedTextField(value = state.storeName, onValueChange = viewModel::onStoreNameChange, label = { Text("Nombre de la tienda") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state.phone, onValueChange = viewModel::onPhoneChange, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state.whatsapp, onValueChange = viewModel::onWhatsappChange, label = { Text("Whatsapp") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state.reportEmail, onValueChange = viewModel::onReportEmailChange, label = { Text("Email de reporte") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state.address, onValueChange = viewModel::onAddressChange, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state.city, onValueChange = viewModel::onCityChange, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.isStoreVisible, onCheckedChange = viewModel::onStoreVisibilityChange)
            Text("Tienda visible al público")
        }
    }
}

@Composable
private fun AppearanceSettingsSection(state: AppearanceSettingsState, viewModel: SettingsViewModel) {
    val logoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        viewModel.onLogoUriChange(uri)
    }
    val coverPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        viewModel.onCoverUriChange(uri)
    }

    SettingsCard(title = "Apariencia", onSave = { viewModel.saveAppearanceSettings() }) {
        // Selector de tema
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = state.isDarkMode, onCheckedChange = viewModel::onDarkModeChange)
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp)) // Separador

        Text("Logo actual", style = MaterialTheme.typography.titleMedium)
        ImagePreview(uri = state.logoUri, modifier = Modifier.size(120.dp))
        Button(onClick = { logoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text("Elegir logo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Portada actual", style = MaterialTheme.typography.titleMedium)
        ImagePreview(uri = state.coverUri, modifier = Modifier.fillMaxWidth().height(180.dp))
        Button(onClick = { coverPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text("Elegir portada")
        }
    }
}

@Composable
private fun PaymentsSettingsSection(state: PaymentsSettingsState, viewModel: SettingsViewModel) {
    SettingsCard(title = "Pagos", onSave = { viewModel.savePaymentsSettings() }) {
        OutlinedTextField(value = state.instructions, onValueChange = viewModel::onInstructionsChange, label = { Text("Instrucciones de pago") }, modifier = Modifier.fillMaxWidth().height(120.dp))
    }
}

@Composable
private fun ShippingSettingsSection(state: ShippingSettingsState, viewModel: SettingsViewModel) {
    SettingsCard(title = "Envíos", onSave = { viewModel.saveShippingSettings() }) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = state.radius, onValueChange = viewModel::onRadiusChange, label = { Text("Radio (km)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = state.baseCost, onValueChange = viewModel::onBaseCostChange, label = { Text("Costo base (S/.)") }, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun TaxesSettingsSection(state: TaxesSettingsState, viewModel: SettingsViewModel) {
    SettingsCard(title = "Impuestos", onSave = { viewModel.saveTaxesSettings() }) {
        OutlinedTextField(value = state.percentage, onValueChange = viewModel::onPercentageChange, label = { Text("Porcentaje (%)") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.pricesIncludeTax, onCheckedChange = viewModel::onPricesIncludeTaxChange)
            Text("Los precios incluyen impuesto")
        }
    }
}

@Composable
private fun NotificationsSettingsSection(state: NotificationsSettingsState, viewModel: SettingsViewModel) {
    SettingsCard(title = "Notificaciones", onSave = { viewModel.saveNotificationsSettings() }) {
        OutlinedTextField(value = state.email, onValueChange = viewModel::onNotificationsEmailChange, label = { Text("Email para notificaciones") }, modifier = Modifier.fillMaxWidth())
    }
}

// Componente reutilizable para las tarjetas de configuración
@Composable
private fun SettingsCard(title: String, onSave: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
                Button(onClick = onSave, modifier = Modifier.align(Alignment.End)) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}

@Composable
private fun ImagePreview(uri: Uri?, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        if (uri != null) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = "No hay imagen", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
            }
        }
    }
}

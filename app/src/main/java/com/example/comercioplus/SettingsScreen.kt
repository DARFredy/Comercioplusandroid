package com.example.comercioplus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("General", "Apariencia", "Pagos", "Envíos", "Impuestos", "Notificaciones")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) })
            }
        }

        // Contenido de las pestañas
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
            when (tabIndex) {
                0 -> GeneralSettings()
                1 -> AppearanceSettings()
                2 -> PaymentsSettings()
                3 -> ShippingSettings()
                4 -> TaxesSettings()
                5 -> NotificationsSettings()
            }
        }
    }
}

@Composable
private fun GeneralSettings() {
    var storeVisible by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsSection(title = "Configuración General") {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Whatsapp") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email de reporte") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = storeVisible, onCheckedChange = { storeVisible = it })
                Text("Tienda visible al público")
            }
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
        }
    }
}

@Composable
private fun AppearanceSettings() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsSection(title = "Apariencia") {
            Text("Logo actual", style = MaterialTheme.typography.titleMedium)
            // Placeholder for logo
            Box(modifier = Modifier.size(100.dp).padding(8.dp), contentAlignment = Alignment.Center) { Text("[Logo]") }
            Button(onClick = { /*TODO*/ }) { Text("Elegir archivo") }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Portada actual", style = MaterialTheme.typography.titleMedium)
            // Placeholder for cover
            Box(modifier = Modifier.fillMaxWidth().height(150.dp).padding(8.dp), contentAlignment = Alignment.Center) { Text("[Portada]") }
            Button(onClick = { /*TODO*/ }) { Text("Elegir archivo") }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
        }
    }
}

@Composable
private fun PaymentsSettings() {
    SettingsSection(title = "Pagos") {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Instrucciones de pago") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
    }
}

@Composable
private fun ShippingSettings() {
    SettingsSection(title = "Envíos") {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Radio de envío (km)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Costo base de envío (S/.)") }, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
    }
}

@Composable
private fun TaxesSettings() {
    var pricesIncludeTax by remember { mutableStateOf(false) }
    SettingsSection(title = "Impuestos") {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Porcentaje de impuesto (%)") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = pricesIncludeTax, onCheckedChange = { pricesIncludeTax = it })
            Text("Los precios incluyen impuesto")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
    }
}

@Composable
private fun NotificationsSettings() {
    SettingsSection(title = "Notificaciones") {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email para notificaciones") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) { Text("Guardar cambios") }
    }
}


@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}
package com.example.comercioplus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comercioplus.ui.theme.ComercioplusTheme

@Composable
fun AnalyticsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Título
        Text(
            text = "Resumen de Analíticas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Fila de tarjetas superior
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AnalyticsStatCard(title = "Eventos últimas 24h", value = "0")
                AnalyticsStatCard(title = "Favoritos guardados (7 días)", value = "0")
            }
            // Tarjeta de estado
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Estado", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFFFA500)) // Naranja
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Con errores", color = Color.White)
                    }
                }
            }
        }

        // Fila de tarjetas inferior
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsStatCard(title = "Likes por día (7 días)", value = "1", modifier = Modifier.weight(1f))
            AnalyticsStatCard(title = "Búsquedas por día (7 días)", value = "0", modifier = Modifier.weight(1f))
        }

        // Placeholder para el gráfico
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)) // Un color de fondo rosado claro como en la imagen
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gráfico en construcción", color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    ComercioplusTheme {
        AnalyticsScreen()
    }
}

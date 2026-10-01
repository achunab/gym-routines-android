package com.example.gymroutines

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun HistorialScreen(viewModel: EntrenoViewModel) {
    val historial by viewModel.historial.collectAsState(initial = emptyList())
    val racha by viewModel.racha.collectAsState(initial = 0)
    var mostrarAnimacion by remember { mutableStateOf(false) }

    // TEMA: LaunchedEffect
    LaunchedEffect(Unit) {
        delay(100) // efecto de entrada
        mostrarAnimacion = true
    }

    val escala by animateFloatAsState(
        targetValue = if (mostrarAnimacion) 1f else 0.8f,
        label = "escala"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .scale(escala)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("🔥 Racha: $racha días", fontSize = 22.sp, style = MaterialTheme.typography.titleLarge)

            Button(
                onClick = { viewModel.borrarHistorial() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("🗑️ Borrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (historial.isEmpty()) {
            Text("Aún no hay entrenos, ¡ve a entrenar caballera! ♓️")
        } else {
            LazyColumn {
                items(historial) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📅 ${item.fecha} - ${item.tipo}")
                            Text("💪 ${item.seriesCompletadas} ejercicios")
                        }
                    }
                }
            }
        }
    }
}
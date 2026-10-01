package com.example.gymroutines

import android.content.Intent
import android.media.ToneGenerator
import android.media.AudioManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymroutines.data.Entreno

@Composable
fun HomeScreen(viewModel: EntrenoViewModel){
    val state by viewModel.uiState.collectAsState()
    val toneGen = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }
    // ANTES:
    val historial by viewModel.historial.collectAsState(initial = emptyList())

    // DESPUÉS:
    //val historial = viewModel.historial//.value // o viewModel.historial si ya es List

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ninon - Pantalla principal ♓️", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Rutina: ${state.rutinaActual}", fontSize = 18.sp)
                Text("Ejercicio: ${state.ejercicioActual} / ${state.totalEjercicios}")
                Spacer(Modifier.height(8.dp))
                if(state.enEntreno){
                    Text("SERIE ${state.serieActual} - REP ${state.repActual}/15", fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(progress = { state.progreso }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Text(state.mensaje)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Tu progreso esta semana 📈", style = MaterialTheme.typography.titleLarge)
        ProgressChart(historial = historial)
        Button(
            onClick = {
                viewModel.iniciarEntreno(
                    onSerieTerminada = { toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400) },
                    onEntrenoTerminado = { toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 600) }
                )
            },
            enabled = !state.enEntreno,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(if(state.enEntreno) "🔥 ENTRENANDO ${state.repActual}/15..." else "▶️ INICIAR 4x15")
        }

        Spacer(Modifier.height(8.dp))
        Button(onClick = { viewModel.siguienteEjercicio() }, modifier = Modifier.fillMaxWidth()) {
            Text("Siguiente Ejercicio ➡️")
        }
        //modo prueba de notificación desde botón
        /*val context = LocalContext.current
        Button(onClick = {
            // Lanza la notificación de lunes al instante
            val intent = Intent(context, NotificationReceiver::class.java)
            context.sendBroadcast(intent)
        }) { Text("🧪 PROBAR NOTIFICACIÓN") }*/
    }
}
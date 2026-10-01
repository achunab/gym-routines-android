package com.example.gymroutines

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.gymroutines.data.AppDatabase
import com.example.gymroutines.data.Entreno
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EntrenoUiState(
    val rutinaActual: String = "Piernas + Glúteo",
    val ejercicioActual: Int = 1,
    val totalEjercicios: Int = 6,
    val serieActual: Int = 0,
    val repActual: Int = 0,
    //val mensaje: String = "Lista para darle, Ninon 💪",
    val mensaje: String = "Lista para darle, con todo 💪",
    val enEntreno: Boolean = false,
    val rutinasSemana: Int = 0
) {
    val progreso: Float
        get() = if (serieActual == 0) (ejercicioActual-1).toFloat()/totalEjercicios
        else ((serieActual - 1) * 15 + repActual).toFloat() / 60f
}

class EntrenoViewModel(application: Application) : AndroidViewModel(application) {
    //private val db = Room.databaseBuilder(application, AppDatabase::class.java, "gym_db").build()
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.entrenoDao()

    val historial = dao.obtenerTodos()
    val racha = historial.map { lista -> lista.map { it.fecha.take(11) }.distinct().size }

    private val prefs = application.getSharedPreferences("zazil_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        EntrenoUiState(rutinasSemana = prefs.getInt("rutinas", 0))
    )
    val uiState: StateFlow<EntrenoUiState> = _uiState.asStateFlow()

    fun completarSerie() {
        _uiState.update { it.copy(ejercicioActual = (it.ejercicioActual + 1).coerceAtMost(it.totalEjercicios)) }
    }

    fun siguienteEjercicio() {
        _uiState.update { it.copy(ejercicioActual = (it.ejercicioActual + 1).coerceAtMost(it.totalEjercicios)) }
    }

    fun iniciarEntreno(onSerieTerminada: () -> Unit, onEntrenoTerminado: () -> Unit) {
        if (_uiState.value.enEntreno) return
        viewModelScope.launch {
            _uiState.update { it.copy(enEntreno = true) }
            for (s in 1..4) {
                //_uiState.update { it.copy(serieActual = s, mensaje = "¡Dale serie $s, Ninon! 💪") }
                _uiState.update { it.copy(serieActual = s, mensaje = "¡Dale serie $s, vamos! 💪") }
                for (r in 1..15) {
                    _uiState.update { it.copy(repActual = r) }
                    delay(1000)
                }
                onSerieTerminada() // <- aquí suena el BEEP
                if (s < 4) {
                    _uiState.update { it.copy(mensaje = "¡Serie $s lista! Descanso 3s 💧") }
                    delay(3000)
                }
            }
            val fecha = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault()).format(Date())
            dao.insertar(Entreno(fecha = fecha))
            onEntrenoTerminado()
            val nuevas = _uiState.value.rutinasSemana + 1
            prefs.edit().putInt("rutinas", nuevas).apply()
            _uiState.update {
                it.copy(rutinasSemana = nuevas, mensaje = "¡TERMINASTE! $nuevas esta semana 🔋", serieActual = 0, repActual = 0, enEntreno = false)
            }
        }
    }

    fun borrarHistorial() {
        viewModelScope.launch {
            dao.borrarTodo()
            prefs.edit().putInt("rutinas", 0).apply()
            _uiState.update { it.copy(rutinasSemana = 0, mensaje = "¡Historial limpio! ✨") }
        }
    }
}
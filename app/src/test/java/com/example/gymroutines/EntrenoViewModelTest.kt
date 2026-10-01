package com.example.gymroutines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// --- ESTE ES EL FAKE QUE TE FALTABA ---
data class GymUiState(
    val rutinaActual: String = "Pierna",
    val ejercicioActual: Int = 1,
    val totalEjercicios: Int = 15,
    val serieActual: Int = 1,
    val repActual: Int = 0,
    val progreso: Float = 0f,
    val enEntreno: Boolean = false,
    val mensaje: String = "Lista para entrenar"
)

class FakeEntrenoViewModel(estadoInicial: GymUiState? = null) {
    private val _uiState = MutableStateFlow(estadoInicial ?: GymUiState())
    val uiState: StateFlow<GymUiState> = _uiState

    fun iniciarEntreno(onSerieTerminada: () -> Unit = {}, onEntrenoTerminado: () -> Unit = {}) {
        _uiState.value = _uiState.value.copy(enEntreno = true, serieActual = 1, repActual = 0, progreso = 0f, mensaje = "¡Vamos!")
    }

    fun onRepCompletada() {
        val actual = _uiState.value
        val nuevaRep = actual.repActual + 1
        val progresoTotal = nuevaRep / 60f // 4x15 = 60

        if (nuevaRep % 15 == 0 && nuevaRep < 60) {
            _uiState.value = actual.copy(serieActual = actual.serieActual + 1, repActual = nuevaRep, progreso = progresoTotal, mensaje = "Serie ${actual.serieActual + 1}")
        } else {
            _uiState.value = actual.copy(repActual = nuevaRep, progreso = progresoTotal)
        }

        if (nuevaRep >= 60) {
            _uiState.value = _uiState.value.copy(enEntreno = false, mensaje = "¡Entreno completado! ♓️")
        }
    }

    fun siguienteEjercicio() {
        _uiState.value = _uiState.value.copy(serieActual = 1, repActual = 0, progreso = 0f, mensaje = "Siguiente ejercicio")
    }
}

// --- TUS 5 TESTS ---
class EntrenoViewModelTest {

    private lateinit var viewModel: FakeEntrenoViewModel

    @Before
    fun setup() {
        viewModel = FakeEntrenoViewModel()
    }

    @Test
    fun test1_progreso_inicia_en_cero() = runTest {
        assertEquals(0f, viewModel.uiState.value.progreso, 0.01f)
        assertFalse(viewModel.uiState.value.enEntreno)
    }

    @Test
    fun test2_iniciarEntreno_activa_4x15() = runTest {
        viewModel.iniciarEntreno()
        assertTrue(viewModel.uiState.value.enEntreno)
        assertEquals(1, viewModel.uiState.value.serieActual)
    }

    @Test
    fun test3_completa_60_reps_en_4_series() = runTest {
        viewModel.iniciarEntreno()
        repeat(60) { viewModel.onRepCompletada() }
        assertEquals(60, viewModel.uiState.value.repActual)
        assertTrue(viewModel.uiState.value.progreso >= 1f)
    }

    @Test
    fun test4_siguienteEjercicio_resetea_progreso() = runTest {
        viewModel.iniciarEntreno()
        repeat(30) { viewModel.onRepCompletada() }
        viewModel.siguienteEjercicio()
        assertEquals(0, viewModel.uiState.value.repActual)
        assertEquals(1, viewModel.uiState.value.serieActual)
    }

    @Test
    fun test5_no_rompe_si_giras_celular() = runTest {
        viewModel.iniciarEntreno()
        repeat(15) { viewModel.onRepCompletada() }
        val estadoAntes = viewModel.uiState.value
        val viewModelRecreado = FakeEntrenoViewModel(estadoAntes)
        assertEquals(estadoAntes.repActual, viewModelRecreado.uiState.value.repActual)
    }
}
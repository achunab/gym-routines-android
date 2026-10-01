package com.example.gymroutines

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.dp
import com.example.gymroutines.data.Entreno

@Composable
fun ProgressChart(historial: List<Entreno>) {
    if (historial.isEmpty()) {
        Text("Aún no hay datos, ¡pero hoy ya guardaste uno! 📈", modifier = Modifier.padding(16.dp))
        return
    }

    // Agrupamos por fecha - últimos 7 entrenos para la gráfica
    val ultimos7 = historial.takeLast(7)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(16.dp)
    ) {
        if (ultimos7.size < 2) {
            // Si solo hay 1 punto, dibujamos un puntito
            drawCircle(Color(0xFFE91E63), radius = 12f, center = center)
            return@Canvas
        }

        val max = 4f // tu meta 4x15
        val points = ultimos7.mapIndexed { index, _ ->
            // Por ahora cada entreno vale 1, luego lo hacemos por series
            val x = size.width * index / (ultimos7.size - 1)
            val y = size.height * 0.2f // arriba = ¡entrenaste!
            Offset(x, y)
        }

        // Línea Piscis ♓️
        drawPoints(points, PointMode.Polygon, Color(0xFFE91E63), strokeWidth = 10f)
        // Puntitos
        points.forEach { drawCircle(Color(0xFFAD1457), radius = 10f, center = it) }
    }
}
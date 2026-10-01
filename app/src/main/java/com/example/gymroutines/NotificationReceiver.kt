package com.example.gymroutines

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        val channelId = "lunes_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Rutinas a full", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        // Obtener el día actual y que concuerde
        // para mostrar en la notificación al usuario
        var dia_actual_en_ingles = java.time.LocalDate.now().dayOfWeek
        var numero_dia_actual = dia_actual_en_ingles.value
        var dia_actual_en_espanol ="que dia es hoy"
        when (numero_dia_actual) {
            1 -> dia_actual_en_espanol = "Lunes"
            2 -> dia_actual_en_espanol = "Martes"
            3 -> dia_actual_en_espanol = "Miércoles"
            4 -> dia_actual_en_espanol = "Jueves"
            5 -> dia_actual_en_espanol = "Viernes"
            6 -> dia_actual_en_espanol = "Sábado"
            7 -> dia_actual_en_espanol = "Domingo"
        }
        /*"Monday" -> dia_actual_en_espanol = "Lunes"
        "Tuesday" -> dia_actual_en_espanol = "Martes"
        "Wednesday" -> dia_actual_en_espanol = "Miércoles"
        "Thursday" -> dia_actual_en_espanol = "Jueves"
        "Friday" -> dia_actual_en_espanol = "Viernes"
        "Saturday" -> dia_actual_en_espanol = "Sábado"
        "Sunday" -> dia_actual_en_espanol = "Domingo"
            else -> {
                print("dia_actual_numero")
            }
        }*/

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("¡ $dia_actual_en_espanol ! Hora de rutinas 💪♓️")
            //.setContentTitle("¡Lunes! Hora de rutinas 💪♓️")
            //.setContentText("Bella, tu cosmos te llama. ¡A darle a las Piernas + Glúteos!")
            .setContentText("¡Venga vamos a darle a las Piernas + Glúteos!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)

        // Reprogramar para el próximo lunes automáticamente
        //AlarmScheduler.scheduleNextMonday(context)

        // Antes:
        // AlarmScheduler.scheduleNextMonday(context)

        // Actualización 6 días notifica menos domingo
        AlarmScheduler.scheduleLunesASabado545(context)
    }
}
package com.example.gymroutines

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gymroutines.data.AppDatabase
import java.util.concurrent.TimeUnit

class EntrenoReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.entrenoDao()
        val historial = dao.obtenerTodosSync() // Necesitamos uno nuevo sin Flow

        val hace2Dias = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)
        val entrenoReciente = historial.any { entreno ->
            try {
                val formato = java.text.SimpleDateFormat("dd MMM yyyy - HH:mm", java.util.Locale.getDefault())
                val fecha = formato.parse(entreno.fecha)?.time ?: 0
                fecha > hace2Dias
            } catch (e: Exception) { false }
        }

        if (!entrenoReciente && historial.isNotEmpty()) {
            mostrarNoti()
        }
        // Si historial está vacío, no molestamos los primeros días
        return Result.success()
    }

    private fun mostrarNoti() {
        val channelId = "racha_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Racha", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val noti = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("¡Tu racha se enfría! 🧊")
            //.setContentText("ZaziL, llevas 2 días sin darle. ¿Hoy retomamos? 💪")
            .setContentText("Llevas 2 días sin entrenar. ¿Hoy retomamos? 💪")
            .setAutoCancel(true)
            .build()
        manager.notify(1002, noti)
    }
}

/*class EntrenoReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.entrenoDao()
        val historial = dao.obtenerTodosSync()

        if (historial.isEmpty()) return Result.success()

        val hace2Dias = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)
        val entrenoReciente = historial.any { it.timestamp > hace2Dias } // guarda timestamp en Long, no String

        if (!entrenoReciente) {
            mostrarNoti()
        }
        // Reprograma la alarma de lunes por si el sistema la borró
        AlarmScheduler.scheduleNextMonday(applicationContext)
        return Result.success()
    }

    private fun mostrarNoti() {
        // código
    }
}*/
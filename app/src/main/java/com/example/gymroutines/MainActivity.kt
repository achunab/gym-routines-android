package com.example.gymroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymroutines.ui.theme.FitZaziLTheme
import android.os.Build
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dentro de onCreate, antes de setContent:
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        //AlarmScheduler.scheduleNextMonday(this)
        AlarmScheduler.scheduleLunesASabado545(this)

        val workRequest = PeriodicWorkRequestBuilder<EntrenoReminderWorker>(12, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "checar_racha",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // --- MODO PRUEBA: Forza la revisión con WorkManager ahora mismo ---
        //val testRequest = androidx.work.OneTimeWorkRequestBuilder<EntrenoReminderWorker>().build()
        //WorkManager.getInstance(this).enqueue(testRequest)
        //--- FIN MODO PRUEBA ---

        setContent {
            FitZaziLTheme {
                val viewModel: EntrenoViewModel = viewModel()
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                            listOf(Screens.Home, Screens.Historial).forEach { screen ->
                            //listOf(Screens.Home, Screens.Acerca).forEach { screen ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        if (currentRoute != screen.route) {
                                            navController.navigate(screen.route)
                                        }
                                    },
                                    icon = { Text(screen.emoji, fontSize = 22.sp) },
                                    label = { Text(screen.label) }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Home.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Screens.Home.route) {
                            HomeScreen(viewModel = viewModel)
                        }
                        composable(Screens.Historial.route) {
                            HistorialScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
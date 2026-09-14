package com.example.friocaliente.Navegacion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigacion.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friocaliente.ui.ComoJugarScreen.ExplicacionScreen
import com.example.friocaliente.ui.DashboardScreen.DashBoardScreen
import com.example.friocaliente.ui.GameScreen.GameScreen


@Composable
fun Navegacion() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {

            // ==========================================
            // DASHBOARD
            // ==========================================

            composable("dashboard") {

                DashBoardScreen(

                    onNuevaPartida = {
                        navController.navigate("game")
                    },

                    onComoJugar = {
                        navController.navigate("explicacion")
                    }
                )
            }


            // ==========================================
            // EXPLICACIÓN
            // ==========================================

            composable("explicacion") {

                ExplicacionScreen(
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }


            // ==========================================
            // JUEGO
            // ==========================================

            composable("game") {

                GameScreen(

                    onFinalizarPartida = {
                        navController.navigate("resultado")
                    },

                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }


            // ==========================================
            // RESULTADO
            // ==========================================

            composable("resultado") {

                // Temporalmente queda aquí.
                // Cuando tengamos resultadoScreen.kt
                // conectaremos esta pantalla.

            }
        }
    }
}
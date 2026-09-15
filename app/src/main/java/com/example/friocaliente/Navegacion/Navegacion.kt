package com.example.friocaliente.Navegacion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.ResultadoTemporal
import com.example.friocaliente.ui.ExplicacionScreen.ExplicacionScreen
import com.example.friocaliente.ui.DashboardScreen.DashBoardScreen
import com.example.friocaliente.ui.GameScreen.GameScreen
import com.example.friocaliente.ui.ResultadoScreen.resultadoScreen
import kotlin.math.max


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

                val resultado = ResultadoTemporal.ultimo
                val gano = resultado?.fase == FaseJuego.GANADO

                // Ya no arma "titulo", "esNuevoRecord" ni "mejorTiempoPersonal"
                // a mano aquí: le pasamos los datos crudos de la partida y
                // resultadoScreen se encarga de guardar el récord (si aplica),
                // leer el mejor tiempo guardado y armar los textos.
                resultadoScreen(
                    fase = resultado?.fase ?: FaseJuego.PERDIDO,
                    tiempoUtilizadoMs = resultado?.tiempoUtilizadoMs ?: 0L,
                    puntuacionObtenida = resultado?.puntuacion ?: 0,
                    mascotaEmoji = if (gano) "🦫" else "😢",
                    precision = formatearPrecision(resultado?.diferenciaAngularFinal),
                    onJugarDeNuevo = {
                        navController.navigate("game") {
                            popUpTo("dashboard")
                        }
                    },
                    onVolverAlMenu = {
                        navController.popBackStack("dashboard", inclusive = false)
                    }
                )
            }
        }
    }
}

/**
 * Convierte la diferencia angular final (en grados, 0-180) a un porcentaje
 * de "precisión" aproximado para mostrar en la UI. Es una fórmula simple
 * (no la oficial de puntuación, esa vive en LogicaJuego.calcularPuntuacion):
 * 0° de diferencia = 100% de precisión, 50°+ de diferencia = 0%.
 * Se puede ajustar el "50" si el equipo quiere una escala distinta.
 */
private fun formatearPrecision(diferenciaGrados: Float?): String {
    if (diferenciaGrados == null) return "0 %"
    val porcentaje = max(0f, 100f - (diferenciaGrados / 50f) * 100f).toInt()
    return "$porcentaje %"
}

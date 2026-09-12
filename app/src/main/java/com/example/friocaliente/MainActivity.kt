package com.example.friocaliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friocaliente.ui.ComoJugarScreen.ExplicacionScreen
import com.example.friocaliente.ui.DashboardScreen.DashBoardScreen
import com.example.friocaliente.ui.theme.FrioCalienteTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            FrioCalienteTheme {

                AppNavigation()

            }
        }
    }
}


@Composable
fun AppNavigation() {

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
                        // Lo hacemos después
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardPreview() {

    FrioCalienteTheme {

        DashBoardScreen(
            onNuevaPartida = {},
            onComoJugar = {}
        )
    }
}
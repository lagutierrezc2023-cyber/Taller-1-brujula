package com.example.friocaliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.friocaliente.Navegacion.Navegacion
import com.example.friocaliente.ui.DashboardScreen.DashBoardScreen
import com.example.friocaliente.ui.theme.FrioCalienteTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            FrioCalienteTheme {

                Navegacion()

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
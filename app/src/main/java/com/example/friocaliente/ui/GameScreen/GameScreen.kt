package com.example.friocaliente.ui.GameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.Data.EstadoTemperatura
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad
import com.example.friocaliente.Logica.ControladorJuego
import com.example.friocaliente.Sensor.BrujulaSensor
import kotlinx.coroutines.delay


@Composable
fun GameScreen(
    onFinalizarPartida: () -> Unit = {},
    onVolver: () -> Unit = {}
) {

    val context = LocalContext.current

    // =====================================================
    // CONTROLADOR DEL JUEGO
    // =====================================================

    val controlador = remember {
        ControladorJuego(NivelDificultad.MEDIO)
    }


    // =====================================================
    // SENSOR
    // =====================================================

    val brujula = remember {
        BrujulaSensor(context)
    }


    // =====================================================
    // ESTADO DEL JUEGO
    // =====================================================

    var estado by remember {
        mutableStateOf(
            controlador.iniciarPartida()
        )
    }


    // =====================================================
    // INICIAR Y DETENER SENSOR
    // =====================================================

    DisposableEffect(Unit) {

        brujula.iniciar()

        onDispose {
            brujula.detener()
        }
    }


    // =====================================================
    // LEER ORIENTACIÓN DEL SENSOR
    // =====================================================

    LaunchedEffect(Unit) {

        brujula.orientacionGrados.collect { grados ->

            estado = controlador.actualizarOrientacion(grados)
        }
    }


    // =====================================================
    // TEMPORIZADOR
    // =====================================================

    LaunchedEffect(Unit) {

        while (true) {

            delay(100L)

            estado = controlador.actualizarTiempo(100L)

            if (estado.fase != FaseJuego.JUGANDO) {
                break
            }
        }
    }


    // =====================================================
    // CUANDO TERMINA LA PARTIDA
    // =====================================================

    LaunchedEffect(estado.fase) {

        if (estado.fase != FaseJuego.JUGANDO) {

            delay(1000L)

            onFinalizarPartida()
        }
    }


    // =====================================================
    // INTERFAZ
    // =====================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF2F7F1)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // =================================================
        // TÍTULO
        // =================================================

        Text(
            text = "🔥 ENCUÉNTRALO 🔥",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1B5E20),
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =================================================
        // TEMPORIZADOR
        // =================================================

        Temporizador(
            tiempoRestanteMs = estado.tiempoRestanteMs
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =================================================
        // DIRECCIÓN ACTUAL
        // =================================================

        DireccionCard(
            direccionActual = estado.direccionActual
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =================================================
        // ESTADO FRÍO / TIBIO / CALIENTE
        // =================================================

        TemperaturaCard(
            estadoTemperatura = estado.estadoTemperatura
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // =================================================
        // DIFERENCIA ANGULAR
        // =================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Distancia a la dirección correcta",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "${estado.diferenciaAngular.toInt()}°",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B5E20)
                )
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =================================================
        // INSTRUCCIÓN
        // =================================================

        Text(
            text = "📱 Gira y mueve tu teléfono para buscar al personaje",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.weight(1f)
        )


        // =================================================
        // BOTÓN REINICIAR
        // =================================================

        Button(
            onClick = {

                estado = controlador.reiniciar()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {

            Text(
                text = "🔄 REINICIAR",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =================================================
        // BOTÓN VOLVER
        // =================================================

        Button(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray
            )
        ) {

            Text(
                text = "← VOLVER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =========================================================
// TEMPORIZADOR
// =========================================================

@Composable
fun Temporizador(
    tiempoRestanteMs: Long
) {

    val segundos = tiempoRestanteMs / 1000

    val minutos = segundos / 60

    val segundosRestantes = segundos % 60

    val tiempoTexto = String.format(
        "%02d:%02d",
        minutos,
        segundosRestantes
    )


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "⏱️ TIEMPO RESTANTE",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = tiempoTexto,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (segundos <= 10) {
                    Color.Red
                } else {
                    Color(0xFF1B5E20)
                }
            )
        }
    }
}


// =========================================================
// DIRECCIÓN
// =========================================================

@Composable
fun DireccionCard(
    direccionActual: Float
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🧭 DIRECCIÓN ACTUAL",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${direccionActual.toInt()}°",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B5E20)
            )
        }
    }
}


// =========================================================
// TEMPERATURA
// =========================================================

@Composable
fun TemperaturaCard(
    estadoTemperatura: EstadoTemperatura
) {

    val emoji: String
    val texto: String
    val color: Color


    when (estadoTemperatura) {

        EstadoTemperatura.FRIO -> {

            emoji = "🧊"

            texto = "FRÍO"

            color = Color(0xFF1565C0)
        }


        EstadoTemperatura.TIBIO -> {

            emoji = "🌤️"

            texto = "TIBIO"

            color = Color(0xFFFF8F00)
        }


        EstadoTemperatura.CALIENTE -> {

            emoji = "🔥"

            texto = "¡CALIENTE!"

            color = Color(0xFFD32F2F)
        }
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = emoji,
                fontSize = 50.sp
            )

            Text(
                text = texto,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = when (estadoTemperatura) {

                    EstadoTemperatura.FRIO ->
                        "Estás lejos de la dirección objetivo."

                    EstadoTemperatura.TIBIO ->
                        "Te estás acercando."

                    EstadoTemperatura.CALIENTE ->
                        "¡Estás muy cerca!"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
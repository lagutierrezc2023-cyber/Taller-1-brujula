package com.example.friocaliente.ui.ComoJugarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.R
import com.example.friocaliente.ui.theme.BlancoTarjeta
import com.example.friocaliente.ui.theme.TextoApagado
import com.example.friocaliente.ui.theme.TextoOscuro
import com.example.friocaliente.ui.theme.VerdeBosque


@Composable
fun ExplicacionScreen(
    onVolver: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                R.drawable.parallax_background_forest
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿CÓMO JUGAR?",
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
            Text(
                text = "Conviértete en un explorador",
                color = Color.White.copy(alpha = 0.90f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
            TarjetaInstruccion(
                emoji = "🧭",
                titulo = "ENCUENTRA AL PERSONAJE",
                texto = "El personaje está escondido en una dirección "
                        + "aleatoria. Tu objetivo es encontrarlo antes "
                        + "de que se acabe el tiempo."
            )
            Spacer(
                modifier = Modifier.height(14.dp)
            )
            TarjetaInstruccion(
                emoji = "📱",
                titulo = "1. MUEVE TU TELÉFONO",
                texto = "Gira y mueve físicamente tu teléfono para "
                        + "buscar la dirección donde se encuentra "
                        + "el personaje."
            )
            Spacer(
                modifier = Modifier.height(14.dp)
            )
            TarjetaInstruccion(
                emoji = "🌡️",
                titulo = "2. SIGUE LA TEMPERATURA",
                texto = "La temperatura te indica qué tan cerca "
                        + "estás de la dirección correcta."
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            EstadosTemperatura()
            Spacer(
                modifier = Modifier.height(14.dp)
            )
            TarjetaInstruccion(
                emoji = "⏱️",
                titulo = "3. ¡NO PIERDAS TIEMPO!",
                texto = "El juego tiene un tiempo límite. Debes "
                        + "encontrar al personaje antes de que el "
                        + "temporizador llegue a cero."
            )
            Spacer(
                modifier = Modifier.height(14.dp)
            )
            TarjetaInstruccion(
                emoji = "🏆",
                titulo = "4. CONSIGUE LA MEJOR PUNTUACIÓN",
                texto = "Tu puntuación depende del tiempo que tardes "
                        + "y de la precisión con la que encuentres "
                        + "al personaje."
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // REINICIO
            // =================================================

            TarjetaInstruccion(
                emoji = "🔄",
                titulo = "5. PUEDES REINICIAR",
                texto = "Si quieres comenzar de nuevo, puedes "
                        + "reiniciar la partida. Se generará una "
                        + "nueva posición para el personaje."
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // =================================================
            // RESUMEN
            // =================================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VerdeBosque.copy(alpha = 0.95f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "🎯 EN RESUMEN",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Mueve tu teléfono → sigue la temperatura "
                                + "→ encuentra al personaje → consigue "
                                + "la mayor puntuación.",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeBosque
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "← VOLVER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}
@Composable
fun TarjetaInstruccion(
    emoji: String,
    titulo: String,
    texto: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlancoTarjeta.copy(alpha = 0.96f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 27.sp
                )
                Spacer(
                    modifier = Modifier.width(10.dp)
                )
                Text(
                    text = titulo,
                    color = TextoOscuro,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = texto,
                color = TextoApagado,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 21.sp
            )
        }
    }
}
@Composable
fun EstadosTemperatura() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.94f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            EstadoTemperatura(
                emoji = "🧊",
                nombre = "FRÍO",
                descripcion = "Estás lejos de la dirección objetivo."
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            EstadoTemperatura(
                emoji = "🌤️",
                nombre = "TIBIO",
                descripcion = "Te estás acercando a la dirección correcta."
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            EstadoTemperatura(
                emoji = "🔥",
                nombre = "CALIENTE",
                descripcion = "¡Estás muy cerca! Sigue buscando."
            )
        }
    }
}
@Composable
fun EstadoTemperatura(
    emoji: String,
    nombre: String,
    descripcion: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 26.sp
        )
        Spacer(
            modifier = Modifier.width(10.dp)
        )
        Column {
            Text(
                text = nombre,
                color = TextoOscuro,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = descripcion,
                color = TextoApagado,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
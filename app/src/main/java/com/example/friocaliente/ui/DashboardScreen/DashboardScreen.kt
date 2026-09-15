package com.example.friocaliente.ui.DashboardScreen

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.R
import com.example.friocaliente.Data.PreferenciasPuntajes
import com.example.friocaliente.Data.formatearTiempoMmSs
import com.example.friocaliente.ui.theme.BlancoTarjeta
import com.example.friocaliente.ui.theme.TextoApagado
import com.example.friocaliente.ui.theme.TextoOscuro
import com.example.friocaliente.ui.theme.VerdeBosque
import com.example.friocaliente.ui.theme.degradadoCaliente
import com.example.friocaliente.ui.theme.degradadoFrio


@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    onNuevaPartida: () -> Unit,
    onComoJugar: () -> Unit
) {

    // Se leen aquí, en cada composición: como Navegacion recrea esta pantalla
    // cada vez que el usuario vuelve al menú, siempre reflejan el valor más
    // reciente guardado por ResultadoScreen al terminar una partida.
    val context = LocalContext.current
    val mejorTiempoMs = PreferenciasPuntajes.obtenerMejorTiempoMs(context)
    val mejorPuntuacion = PreferenciasPuntajes.obtenerMejorPuntuacion(context)

    Box(
        modifier = modifier.fillMaxSize()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Titulo()

            Spacer(
                modifier = Modifier.offset(y = (-60).dp)
            )

            CardBienvenida(
                mejorTiempoMs = mejorTiempoMs,
                mejorPuntuacion = mejorPuntuacion
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            BotonesDashboard(
                onNuevaPartida = onNuevaPartida,
                onComoJugar = onComoJugar
            )
        }
    }
}

@Composable
fun Titulo() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Caliente",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        color = Color.Black,
                        drawStyle = Stroke(
                            width = 10f,
                            join = StrokeJoin.Round
                        ),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.75f),
                            offset = Offset(5f, 7f),
                            blurRadius = 5f
                        )
                    )
                )
                Text(
                    text = "Caliente",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        brush = degradadoCaliente
                    )
                )
            }
            Box(
                modifier = Modifier.offset(y = (-26).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FRIO",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        color = Color.Black,
                        drawStyle = Stroke(
                            width = 10f,
                            join = StrokeJoin.Round
                        ),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.75f),
                            offset = Offset(5f, 7f),
                            blurRadius = 5f
                        )
                    )
                )
                Text(
                    text = "FRIO",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        brush = degradadoFrio
                    )
                )
            }
            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    )
                    .offset(y = (-49).dp)
            ) {
                TextoEncuentralo3D()
            }
        }
    }
}
@Composable
fun TextoEncuentralo3D(
    texto: String = "¡Encuéntralo!"
) {
    val doradoBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF3C4),
            Color(0xFFF5B942),
            Color(0xFFC9861B),
            Color(0xFF7A4E0E)
        )
    )
    val negroBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3A3A3A),
            Color(0xFF111111),
            Color(0xFF000000)
        )
    )
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF7A4E0E),
            modifier = Modifier.offset(y = 8.dp)
        )
        Text(
            text = texto,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF7A4E0E),
            modifier = Modifier.offset(y = 6.dp)
        )
        Text(
            text = texto,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF7A4E0E),
            modifier = Modifier.offset(y = 4.dp)
        )
        Text(
            text = texto,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                brush = doradoBrush,
                drawStyle = Stroke(
                    width = 8f,
                    join = StrokeJoin.Round
                ),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.65f),
                    offset = Offset(0f, 6f),
                    blurRadius = 7f
                )
            )
        )
        Text(
            text = texto,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                brush = negroBrush
            )
        )
    }
}

@Composable
fun CardBienvenida(
    mejorTiempoMs: Long = PreferenciasPuntajes.SIN_REGISTRO,
    mejorPuntuacion: Int = 0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlancoTarjeta
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "¡Bienvenido, Explorador!",
                color = TextoOscuro,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
            Text(
                text = "Encuentra el personaje escondido antes de que se acabe el tiempo.",
                color = TextoApagado,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                ItemEstadistica(
                    emoji = "⏱️",
                    etiqueta = "Mejor tiempo",
                    valor = if (mejorTiempoMs == PreferenciasPuntajes.SIN_REGISTRO) {
                        "--:--"
                    } else {
                        formatearTiempoMmSs(mejorTiempoMs)
                    }
                )
                ItemEstadistica(
                    emoji = "🏆",
                    etiqueta = "Mejor puntuación",
                    valor = "$mejorPuntuacion pts"
                )
            }
        }
    }
}
@Composable
fun ItemEstadistica(
    emoji: String,
    etiqueta: String,
    valor: String
) {
    Column {
        Text(
            text = "$emoji $etiqueta",
            color = TextoApagado,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = valor,
            color = TextoOscuro,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun BotonesDashboard(
    onNuevaPartida: () -> Unit,
    onComoJugar: () -> Unit
) {

    Button(
        onClick = onNuevaPartida,
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdeBosque
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .offset(y = (-60).dp)
    ) {
        Text(
            text = "▶ NUEVA PARTIDA",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }

    Spacer(
        modifier = Modifier.height(16.dp)
    )

    OutlinedButton(
        onClick = onComoJugar,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .offset(y = (-60).dp)
    ) {
        Text(
            text = "❓ CÓMO JUGAR",
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            color = TextoOscuro
        )
    }
}

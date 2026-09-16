package com.example.friocaliente.ui.ResultadoScreen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.R
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.PreferenciasPuntajes
import com.example.friocaliente.Data.formatearTiempoMmSs
import com.example.friocaliente.ui.theme.BlancoTarjeta
import com.example.friocaliente.ui.theme.TextoApagado
import com.example.friocaliente.ui.theme.TextoOscuro
import com.example.friocaliente.ui.theme.VerdeBosque

<<<<<<< HEAD
=======

>>>>>>> b33a70482cfb290329aba36b6a025e39a1ba3a79
@Composable
fun resultadoScreen(
    fase: FaseJuego = FaseJuego.GANADO,
    tiempoUtilizadoMs: Long = 0L,
    puntuacionObtenida: Int = 0,
    mascotaEmoji: String = "🦫",
    precision: String = "92 %",
    onJugarDeNuevo: () -> Unit = {},
    onVolverAlMenu: () -> Unit = {},
    onCompartir: () -> Unit = {}
) {
    val context = LocalContext.current
    var esNuevoRecordTiempo by remember { mutableStateOf(false) }
    var mejorTiempoMs by remember { mutableStateOf(PreferenciasPuntajes.SIN_REGISTRO) }

    // Se guarda UNA sola vez por resultado (tiempoUtilizadoMs/puntuacionObtenida
    // como keys) para no volver a comparar en cada recomposición.
    LaunchedEffect(tiempoUtilizadoMs, puntuacionObtenida) {
        if (fase == FaseJuego.GANADO) {
            esNuevoRecordTiempo = PreferenciasPuntajes.guardarSiEsMejor(
                context = context,
                tiempoUtilizadoMs = tiempoUtilizadoMs,
                puntuacion = puntuacionObtenida
            )
        }
        mejorTiempoMs = PreferenciasPuntajes.obtenerMejorTiempoMs(context)
    }

    val titulo = if (fase == FaseJuego.GANADO) "¡LO ENCONTRASTE!" else "SE ACABÓ EL TIEMPO"
    val tiempoTotalTexto = formatearTiempoMmSs(tiempoUtilizadoMs)
    val mejorTiempoTexto = if (mejorTiempoMs == PreferenciasPuntajes.SIN_REGISTRO) {
        tiempoTotalTexto
    } else {
        formatearTiempoMmSs(mejorTiempoMs)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.parallax_background_forest),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TituloResultado(texto = titulo)

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = CircleShape,
                color = BlancoTarjeta,
                modifier = Modifier.size(140.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = mascotaEmoji, fontSize = 64.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoTarjeta),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    FilaResultado(emoji = "⏱️", etiqueta = "Tiempo total", valor = tiempoTotalTexto) {
                        if (esNuevoRecordTiempo) BadgeNuevoRecord()
                    }
                    LineaDivisoria()
                    FilaResultado(emoji = "⭐", etiqueta = "Puntuación", valor = "$puntuacionObtenida pts")
                    LineaDivisoria()
                    FilaResultado(emoji = "🎯", etiqueta = "Precisión", valor = precision)
                    LineaDivisoria()
                    FilaResultado(emoji = "🏅", etiqueta = "Mejor tiempo personal", valor = mejorTiempoTexto)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onJugarDeNuevo,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeBosque),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("🔄  JUGAR DE NUEVO", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolverAlMenu,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, VerdeBosque),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = BlancoTarjeta,
                    contentColor = VerdeBosque
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("🏠  VOLVER AL MENÚ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

        }
    }
}

@Composable
private fun TituloResultado(texto: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = texto,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(
                color = Color.Black,
                drawStyle = Stroke(width = 7f, join = StrokeJoin.Round),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(3f, 4f),
                    blurRadius = 4f
                )
            )
        )
        Text(
            text = texto,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = VerdeBosque
        )
    }
}

@Composable
private fun FilaResultado(
    emoji: String,
    etiqueta: String,
    valor: String,
    badgeExtra: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$emoji  $etiqueta", color = TextoApagado, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = valor, color = TextoOscuro, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            badgeExtra?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it()
            }
        }
    }
}

@Composable
private fun BadgeNuevoRecord() {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = VerdeBosque.copy(alpha = 0.15f)
    ) {
        Text(
            text = "¡Nuevo récord!",
            color = VerdeBosque,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun LineaDivisoria() {
    // Antes esto era un Spacer sin color: ocupaba 1dp de alto pero era
    // invisible. Con background() ahora sí se ve la línea separadora.
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(TextoApagado.copy(alpha = 0.15f))
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun ResultadoScreenPreview() {
    MaterialTheme {
        resultadoScreen(
            tiempoUtilizadoMs = 40_000L,
            puntuacionObtenida = 800
        )
    }
}

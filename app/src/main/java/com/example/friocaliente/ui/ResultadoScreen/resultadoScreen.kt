package com.example.friocaliente.ui.ResultadoScreen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.R
import com.example.friocaliente.ui.theme.BlancoTarjeta
import com.example.friocaliente.ui.theme.TextoApagado
import com.example.friocaliente.ui.theme.TextoOscuro
import com.example.friocaliente.ui.theme.VerdeBosque

@Composable
fun resultadoScreen(
    titulo: String = "¡LO ENCONTRASTE!",
    mascotaEmoji: String = "🦫",
    tiempoTotal: String = "00:32",
    esNuevoRecord: Boolean = true,
    puntuacion: String = "850 pts",
    precision: String = "92 %",
    mejorTiempoPersonal: String = "01:45",
    onJugarDeNuevo: () -> Unit = {},
    onVolverAlMenu: () -> Unit = {},
    onCompartir: () -> Unit = {}
) {
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

            Text(
                text = titulo,
                color = VerdeBosque,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

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

                    FilaResultado(emoji = "⏱️", etiqueta = "Tiempo total", valor = tiempoTotal) {
                        if (esNuevoRecord) BadgeNuevoRecord()
                    }
                    LineaDivisoria()
                    FilaResultado(emoji = "⭐", etiqueta = "Puntuación", valor = puntuacion)
                    LineaDivisoria()
                    FilaResultado(emoji = "🎯", etiqueta = "Precisión", valor = precision)
                    LineaDivisoria()
                    FilaResultado(emoji = "🏅", etiqueta = "Mejor tiempo personal", valor = mejorTiempoPersonal)
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
                Text("🔄  JUGAR DE NUEVO", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolverAlMenu,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoOscuro),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("🏠  VOLVER AL MENÚ", fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onCompartir) {
                Text("📤  COMPARTIR PUNTUACIÓN", color = TextoApagado, fontSize = 14.sp)
            }
        }
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
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun ResultadoScreenPreview() {
    MaterialTheme {
        resultadoScreen()
    }
}
package com.example.friocaliente.ui.DashboardScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friocaliente.ui.theme.ColorMadera
import com.example.friocaliente.ui.theme.Naranja

@Composable
fun DashBoardScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Titulo()
        Spacer(modifier = Modifier.height(24.dp))
        CardBienvenida()
        Spacer(modifier = Modifier.height(28.dp))
        BotonesDashboard()

    }
}

@Composable
fun Titulo() {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = ColorMadera)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Caliente", color = Naranja, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
            Text("FRIO", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
            Text("Encuentralo", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun CardBienvenida() {
    Card(

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bienvenido, Explorador", color = Color.Green, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
        }

    }
}

@Composable
fun BotonesDashboard() {

}
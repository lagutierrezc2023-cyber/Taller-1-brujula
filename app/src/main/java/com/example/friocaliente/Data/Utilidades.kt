package com.example.friocaliente.Data

fun formatearTiempoMmSs(tiempoMs: Long): String {
    if (tiempoMs < 0) return "--:--"
    val totalSegundos = tiempoMs / 1000
    val minutos = totalSegundos / 60
    val segundos = totalSegundos % 60
    return "%02d:%02d".format(minutos, segundos)
}

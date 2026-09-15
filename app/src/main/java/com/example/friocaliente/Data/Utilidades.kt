package com.example.friocaliente.Data

/**
 * Convierte milisegundos a un texto "mm:ss" para mostrar en la UI.
 * Si el valor es [PreferenciasPuntajes.SIN_REGISTRO] o negativo, devuelve "--:--".
 *
 * Ejemplos: 40_000L -> "00:40" | 105_000L -> "01:45"
 */
fun formatearTiempoMmSs(tiempoMs: Long): String {
    if (tiempoMs < 0) return "--:--"
    val totalSegundos = tiempoMs / 1000
    val minutos = totalSegundos / 60
    val segundos = totalSegundos % 60
    return "%02d:%02d".format(minutos, segundos)
}

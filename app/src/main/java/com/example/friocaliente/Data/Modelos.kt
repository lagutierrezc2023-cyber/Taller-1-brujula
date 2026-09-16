package com.example.friocaliente.Data


enum class EstadoTemperatura {
    FRIO,
    TIBIO,
    CALIENTE
}


enum class FaseJuego {
    JUGANDO,
    GANADO,
    PERDIDO
}

enum class NivelDificultad(val tiempoLimiteMs: Long, val margenGrados: Float) {
    FACIL(tiempoLimiteMs = 90_000L, margenGrados = 25f),
    MEDIO(tiempoLimiteMs = 60_000L, margenGrados = 15f),
    DIFICIL(tiempoLimiteMs = 40_000L, margenGrados = 8f)
}


data class ResultadoPartida(
    val fase: FaseJuego,
    val tiempoUtilizadoMs: Long,
    val diferenciaAngularFinal: Float,
    val puntuacion: Int
)

package com.example.friocaliente.Data

/**
 * Estado de "temperatura" que se muestra al jugador según qué tan cerca
 * está su orientación actual respecto a la dirección objetivo (donde
 * está escondido el personaje).
 */
enum class EstadoTemperatura {
    FRIO,
    TIBIO,
    CALIENTE
}

/**
 * Fase en la que se encuentra la partida actual.
 */
enum class FaseJuego {
    JUGANDO,
    GANADO,
    PERDIDO
}

/**
 * Niveles de dificultad opcionales (regla opcional del enunciado).
 * Cada nivel define:
 *  - tiempoLimiteMs: tiempo total disponible para encontrar al personaje.
 *  - margenGrados: margen angular (en grados) dentro del cual se considera
 *    que el jugador "encontró" al personaje.
 *
 * Nota para el equipo: estos valores son un punto de partida razonable,
 * se pueden ajustar sin romper nada más porque el resto de la lógica
 * los usa como parámetros, no como constantes fijas.
 */
enum class NivelDificultad(val tiempoLimiteMs: Long, val margenGrados: Float) {
    FACIL(tiempoLimiteMs = 90_000L, margenGrados = 25f),
    MEDIO(tiempoLimiteMs = 60_000L, margenGrados = 15f),
    DIFICIL(tiempoLimiteMs = 40_000L, margenGrados = 8f)
}

/**
 * Resultado final de una partida, calculado una sola vez cuando el juego
 * termina (ya sea porque el jugador ganó o porque se acabó el tiempo).
 *
 * Formula de puntuación (documentada según lo pide el enunciado):
 *
 *  1) Si el jugador PIERDE (se acaba el tiempo sin encontrar al personaje):
 *       puntuacion = 0
 *
 *  2) Si el jugador GANA:
 *       puntuacionBase   = 1000
 *       segundosUsados   = tiempoUtilizadoMs / 1000
 *       penalizacion     = segundosUsados * 5          (baja el puntaje mientras más tiempo tarda)
 *       bonoPrecision    = 200 si diferenciaAngularFinal <= (margenGrados / 2), si no 0
 *       puntuacion       = max(0, puntuacionBase - penalizacion) + bonoPrecision
 *
 *  Esto cumple con: "la puntuación debe aumentar cuando el jugador encuentra
 *  rápido" (penalización por tiempo), "una búsqueda prolongada produce una
 *  puntuación inferior" (misma penalización, crece con el tiempo) y
 *  "bonificación de precisión" (bonoPrecision).
 */
data class ResultadoPartida(
    val fase: FaseJuego,
    val tiempoUtilizadoMs: Long,
    val diferenciaAngularFinal: Float,
    val puntuacion: Int
)

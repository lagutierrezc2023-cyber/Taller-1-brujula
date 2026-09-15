package com.example.friocaliente.Logica

import com.example.friocaliente.Data.EstadoTemperatura
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad

/**
 * Representa una "foto" completa e inmutable del estado del juego en un
 * instante dado. La UI (Compose) debe limitarse a OBSERVAR este objeto y
 * pintarlo; nunca debe calcular reglas de juego por su cuenta. Todo cambio
 * de estado se hace a través de [ControladorJuego].
 *
 * @param direccionObjetivo grados (0-359.9) donde está "escondido" el personaje.
 * @param direccionActual grados (0-359.9) hacia donde apunta el teléfono ahora mismo,
 *                         entregado por el sensor (Sensor/BrujulaSensor.kt).
 * @param diferenciaAngular distancia angular mínima entre direccionActual y
 *                           direccionObjetivo, en el rango [0, 180].
 * @param tiempoRestanteMs tiempo restante del temporizador en milisegundos.
 * @param nivel nivel de dificultad activo para esta partida.
 * @param estadoTemperatura Frío / Tibio / Caliente, calculado a partir de diferenciaAngular.
 * @param fase JUGANDO / GANADO / PERDIDO.
 * @param puntuacion puntuación actual (0 mientras se juega; el valor final se fija
 *                    al terminar la partida, ver [ResultadoPartida]).
 */
data class EstadoJuego(
    val direccionObjetivo: Float,
    val direccionActual: Float,
    val diferenciaAngular: Float,
    val tiempoRestanteMs: Long,
    val nivel: NivelDificultad,
    val estadoTemperatura: EstadoTemperatura,
    val fase: FaseJuego,
    val puntuacion: Int
) {
    companion object {
        /**
         * Construye el estado inicial de una partida nueva.
         */
        fun inicial(nivel: NivelDificultad, direccionObjetivo: Float): EstadoJuego {
            val diferencia = LogicaJuego.calcularDiferenciaAngular(
                direccionActual = 0f,
                direccionObjetivo = direccionObjetivo
            )
            return EstadoJuego(
                direccionObjetivo = direccionObjetivo,
                direccionActual = 0f,
                diferenciaAngular = diferencia,
                tiempoRestanteMs = nivel.tiempoLimiteMs,
                nivel = nivel,
                estadoTemperatura = LogicaJuego.calcularEstadoTemperatura(diferencia, nivel.margenGrados),
                fase = FaseJuego.JUGANDO,
                puntuacion = 0
            )
        }
    }
}

package com.example.friocaliente.Logica

import com.example.friocaliente.Data.EstadoTemperatura
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad


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

package com.example.friocaliente.Logica

import com.example.friocaliente.Data.EstadoTemperatura
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad
import com.example.friocaliente.Data.ResultadoPartida
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random


object LogicaJuego {

    private const val BONO_PRECISION = 200

    private const val PUNTUACION_BASE = 1000

    private const val PENALIZACION_POR_SEGUNDO = 5

    fun generarDireccionObjetivo(random: Random = Random.Default): Float {
        return random.nextFloat() * 360f
    }

    fun calcularDiferenciaAngular(direccionActual: Float, direccionObjetivo: Float): Float {
        val diferenciaCruda = abs(direccionActual - direccionObjetivo) % 360f
        return if (diferenciaCruda > 180f) 360f - diferenciaCruda else diferenciaCruda
    }

    fun calcularEstadoTemperatura(diferenciaAngular: Float, margenGrados: Float): EstadoTemperatura {
        return when {
            diferenciaAngular <= margenGrados * 2f -> EstadoTemperatura.CALIENTE
            diferenciaAngular <= margenGrados * 4f -> EstadoTemperatura.TIBIO
            else -> EstadoTemperatura.FRIO
        }
    }

    fun seEncontroPersonaje(diferenciaAngular: Float, margenGrados: Float): Boolean {
        return diferenciaAngular <= margenGrados
    }

    fun calcularPuntuacion(
        tiempoUtilizadoMs: Long,
        diferenciaAngularFinal: Float,
        margenGrados: Float
    ): Int {
        val segundosUsados = tiempoUtilizadoMs / 1000
        val penalizacion = segundosUsados * PENALIZACION_POR_SEGUNDO
        val bono = if (diferenciaAngularFinal <= margenGrados / 2f) BONO_PRECISION else 0
        val puntuacionConPenalizacion = max(0L, PUNTUACION_BASE - penalizacion)
        return (puntuacionConPenalizacion + bono).toInt()
    }
}

class ControladorJuego(
    nivelInicial: NivelDificultad = NivelDificultad.MEDIO,
    private val random: Random = Random.Default
) {

    private var nivelActual: NivelDificultad = nivelInicial

    private var tiempoTranscurridoMs: Long = 0L

    var estadoActual: EstadoJuego = EstadoJuego.inicial(
        nivel = nivelInicial,
        direccionObjetivo = LogicaJuego.generarDireccionObjetivo(random)
    )
        private set


    fun iniciarPartida(nivel: NivelDificultad = nivelActual): EstadoJuego {
        nivelActual = nivel
        tiempoTranscurridoMs = 0L
        val direccionObjetivo = LogicaJuego.generarDireccionObjetivo(random)
        estadoActual = EstadoJuego.inicial(nivel = nivel, direccionObjetivo = direccionObjetivo)
        return estadoActual
    }

    fun actualizarOrientacion(direccionActual: Float): EstadoJuego {
        if (estadoActual.fase != FaseJuego.JUGANDO) return estadoActual

        val direccionNormalizada = ((direccionActual % 360f) + 360f) % 360f
        val diferencia = LogicaJuego.calcularDiferenciaAngular(
            direccionActual = direccionNormalizada,
            direccionObjetivo = estadoActual.direccionObjetivo
        )
        val encontrado = LogicaJuego.seEncontroPersonaje(diferencia, nivelActual.margenGrados)

        estadoActual = if (encontrado) {
            val puntuacion = LogicaJuego.calcularPuntuacion(
                tiempoUtilizadoMs = tiempoTranscurridoMs,
                diferenciaAngularFinal = diferencia,
                margenGrados = nivelActual.margenGrados
            )
            estadoActual.copy(
                direccionActual = direccionNormalizada,
                diferenciaAngular = diferencia,
                estadoTemperatura = EstadoTemperatura.CALIENTE,
                fase = FaseJuego.GANADO,
                puntuacion = puntuacion
            )
        } else {
            estadoActual.copy(
                direccionActual = direccionNormalizada,
                diferenciaAngular = diferencia,
                estadoTemperatura = LogicaJuego.calcularEstadoTemperatura(diferencia, nivelActual.margenGrados)
            )
        }
        return estadoActual
    }

    fun actualizarTiempo(deltaMs: Long): EstadoJuego {
        if (estadoActual.fase != FaseJuego.JUGANDO) return estadoActual

        tiempoTranscurridoMs += deltaMs
        val tiempoRestante = max(0L, estadoActual.tiempoRestanteMs - deltaMs)

        estadoActual = if (tiempoRestante <= 0L) {
            estadoActual.copy(
                tiempoRestanteMs = 0L,
                fase = FaseJuego.PERDIDO,
                puntuacion = 0
            )
        } else {
            estadoActual.copy(tiempoRestanteMs = tiempoRestante)
        }
        return estadoActual
    }


    fun reiniciar(): EstadoJuego = iniciarPartida(nivelActual)


    fun obtenerResultado(): ResultadoPartida {
        return ResultadoPartida(
            fase = estadoActual.fase,
            tiempoUtilizadoMs = tiempoTranscurridoMs,
            diferenciaAngularFinal = estadoActual.diferenciaAngular,
            puntuacion = estadoActual.puntuacion
        )
    }
}
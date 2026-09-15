package com.example.friocaliente.Logica

import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Pruebas unitarias puras (JUnit) para la lógica del juego.
 * No requieren Android ni instrumentación: van en src/test/java, NO en
 * src/androidTest/java.
 */
class LogicaJuegoTest {

    @Test
    fun `diferencia angular calcula correctamente el camino corto cruzando 0-360`() {
        // Entre 350 y 10 grados, la diferencia real es 20, no 340.
        val diferencia = LogicaJuego.calcularDiferenciaAngular(
            direccionActual = 350f,
            direccionObjetivo = 10f
        )
        assertEquals(20f, diferencia, 0.01f)
    }

    @Test
    fun `diferencia angular es cero cuando las direcciones son iguales`() {
        val diferencia = LogicaJuego.calcularDiferenciaAngular(90f, 90f)
        assertEquals(0f, diferencia, 0.01f)
    }

    @Test
    fun `estado caliente cuando la diferencia es muy pequena`() {
        val estado = LogicaJuego.calcularEstadoTemperatura(diferenciaAngular = 5f, margenGrados = 15f)
        assertEquals(com.example.friocaliente.Data.EstadoTemperatura.CALIENTE, estado)
    }

    @Test
    fun `estado frio cuando la diferencia es muy grande`() {
        val estado = LogicaJuego.calcularEstadoTemperatura(diferenciaAngular = 170f, margenGrados = 15f)
        assertEquals(com.example.friocaliente.Data.EstadoTemperatura.FRIO, estado)
    }

    @Test
    fun `se encuentra el personaje solo dentro del margen del nivel`() {
        assertTrue(LogicaJuego.seEncontroPersonaje(diferenciaAngular = 10f, margenGrados = 15f))
        assertTrue(!LogicaJuego.seEncontroPersonaje(diferenciaAngular = 20f, margenGrados = 15f))
    }

    @Test
    fun `controlador pasa a GANADO al apuntar dentro del margen`() {
        // Semilla fija para que la direccion objetivo sea reproducible en el test.
        val controlador = ControladorJuego(nivelInicial = NivelDificultad.MEDIO, random = Random(42))
        val estadoInicial = controlador.estadoActual
        val objetivo = estadoInicial.direccionObjetivo

        val estadoFinal = controlador.actualizarOrientacion(objetivo)

        assertEquals(FaseJuego.GANADO, estadoFinal.fase)
        assertTrue(estadoFinal.puntuacion > 0)
    }

    @Test
    fun `controlador pasa a PERDIDO cuando se acaba el tiempo`() {
        val controlador = ControladorJuego(nivelInicial = NivelDificultad.DIFICIL)
        val estadoFinal = controlador.actualizarTiempo(NivelDificultad.DIFICIL.tiempoLimiteMs + 1000)

        assertEquals(FaseJuego.PERDIDO, estadoFinal.fase)
        assertEquals(0, estadoFinal.puntuacion)
    }

    @Test
    fun `reiniciar genera una partida nueva en fase JUGANDO`() {
        val controlador = ControladorJuego(nivelInicial = NivelDificultad.FACIL)
        controlador.actualizarTiempo(NivelDificultad.FACIL.tiempoLimiteMs + 100) // pierde la partida
        val estadoReiniciado = controlador.reiniciar()

        assertEquals(FaseJuego.JUGANDO, estadoReiniciado.fase)
        assertEquals(NivelDificultad.FACIL.tiempoLimiteMs, estadoReiniciado.tiempoRestanteMs)
    }
}
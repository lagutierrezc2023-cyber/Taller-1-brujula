package com.example.friocaliente.Data

import android.content.Context

/**
 * Guarda y lee el mejor tiempo y la mejor puntuación del jugador usando
 * SharedPreferences, para que sobrevivan entre partidas y al cerrar la app.
 *
 * Este es el archivo que faltaba: sin él, "mejor tiempo" y "mejor puntuación"
 * nunca podían actualizarse porque no había dónde guardarlos.
 *
 * USO DESDE LA UI:
 *  - Al terminar una partida (fase GANADO), llamar [guardarSiEsMejor] con el
 *    resultado de [ControladorJuego.obtenerResultado] ANTES de navegar a la
 *    pantalla de resultado.
 *  - En Dashboard y en ResultadoScreen, llamar [obtenerMejorTiempoMs] y
 *    [obtenerMejorPuntuacion] para mostrar los valores reales.
 */
object PreferenciasPuntajes {

    private const val NOMBRE_PREFS = "friocaliente_prefs"
    private const val CLAVE_MEJOR_TIEMPO_MS = "mejor_tiempo_ms"
    private const val CLAVE_MEJOR_PUNTUACION = "mejor_puntuacion"

    /** Valor usado cuando todavía no hay ningún registro guardado. */
    const val SIN_REGISTRO = -1L

    fun obtenerMejorTiempoMs(context: Context): Long {
        val prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(CLAVE_MEJOR_TIEMPO_MS, SIN_REGISTRO)
    }

    fun obtenerMejorPuntuacion(context: Context): Int {
        val prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(CLAVE_MEJOR_PUNTUACION, 0)
    }

    /**
     * Compara el resultado de la partida que acaba de terminar contra lo
     * guardado y, si es mejor, lo actualiza. Un tiempo MENOR es mejor;
     * una puntuación MAYOR es mejor. Se evalúan de forma independiente.
     *
     * @return true si se batió el récord de tiempo personal (para mostrar
     *         el badge "¡Nuevo récord!" en la pantalla de resultado).
     */
    fun guardarSiEsMejor(context: Context, tiempoUtilizadoMs: Long, puntuacion: Int): Boolean {
        val prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
        val mejorTiempoActual = prefs.getLong(CLAVE_MEJOR_TIEMPO_MS, SIN_REGISTRO)
        val mejorPuntuacionActual = prefs.getInt(CLAVE_MEJOR_PUNTUACION, 0)

        val esNuevoRecordTiempo = mejorTiempoActual == SIN_REGISTRO || tiempoUtilizadoMs < mejorTiempoActual
        val esNuevaMejorPuntuacion = puntuacion > mejorPuntuacionActual

        if (esNuevoRecordTiempo || esNuevaMejorPuntuacion) {
            prefs.edit().apply {
                if (esNuevoRecordTiempo) putLong(CLAVE_MEJOR_TIEMPO_MS, tiempoUtilizadoMs)
                if (esNuevaMejorPuntuacion) putInt(CLAVE_MEJOR_PUNTUACION, puntuacion)
                apply()
            }
        }

        return esNuevoRecordTiempo
    }
}

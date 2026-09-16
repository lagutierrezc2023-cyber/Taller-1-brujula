package com.example.friocaliente.Data

import android.content.Context


object PreferenciasPuntajes {

    private const val NOMBRE_PREFS = "friocaliente_prefs"
    private const val CLAVE_MEJOR_TIEMPO_MS = "mejor_tiempo_ms"
    private const val CLAVE_MEJOR_PUNTUACION = "mejor_puntuacion"

    const val SIN_REGISTRO = -1L

    fun obtenerMejorTiempoMs(context: Context): Long {
        val prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(CLAVE_MEJOR_TIEMPO_MS, SIN_REGISTRO)
    }

    fun obtenerMejorPuntuacion(context: Context): Int {
        val prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(CLAVE_MEJOR_PUNTUACION, 0)
    }

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

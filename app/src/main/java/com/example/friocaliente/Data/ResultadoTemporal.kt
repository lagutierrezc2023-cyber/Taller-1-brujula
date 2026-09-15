package com.example.friocaliente.Data

/**
 * Solución simple para pasar el [ResultadoPartida] desde GameScreen hasta
 * resultadoScreen sin complicarnos con argumentos de navegación (NavType,
 * navArgument, etc.) ni con un ViewModel compartido.
 *
 * Cómo funciona: GameScreen guarda aquí el resultado justo antes de navegar
 * a "resultado", y la ruta "resultado" lo lee apenas se abre.
 *
 * Nota para el equipo: esto funciona bien para el taller, pero no es la
 * forma "correcta" a largo plazo (un ViewModel compartido a nivel de
 * NavHost sería más robusto). Si el proyecto crece, vale la pena migrar.
 */
object ResultadoTemporal {
    var ultimo: ResultadoPartida? = null
}
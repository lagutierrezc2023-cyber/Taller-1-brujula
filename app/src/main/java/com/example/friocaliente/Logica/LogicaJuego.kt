package com.example.friocaliente.Logica

import com.example.friocaliente.Data.EstadoTemperatura
import com.example.friocaliente.Data.FaseJuego
import com.example.friocaliente.Data.NivelDificultad
import com.example.friocaliente.Data.ResultadoPartida
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

/**
 * ============================================================================
 *  LOGICA DEL JUEGO — motor puro, sin dependencias de Android ni de la UI.
 * ============================================================================
 *
 * Esta clase concentra TODAS las reglas del juego "Escondidas". No importa
 * nada de Compose, Activities ni sensores directamente: solo trabaja con
 * números (grados, milisegundos). Esto permite:
 *   1) Cumplir el requisito del taller: "la lógica del juego no debe
 *      depender directamente de los elementos visuales de la interfaz".
 *   2) Escribir pruebas unitarias simples (JUnit puro, sin Robolectric ni
 *      instrumentación), porque no hay nada de Android que simular.
 *
 * CÓMO LO USA EL RESTO DEL EQUIPO:
 *  - Sensor/BrujulaSensor.kt debe entregar un ángulo (Float, 0-360) que
 *    representa hacia dónde apunta el teléfono.
 *  - La UI / ViewModel (fuera de mi alcance) crea UN [ControladorJuego],
 *    llama [ControladorJuego.iniciarPartida] al empezar, luego en cada
 *    lectura del sensor llama [ControladorJuego.actualizarOrientacion],
 *    y en cada "tick" de un temporizador (por ejemplo cada 100ms con un
 *    coroutine) llama [ControladorJuego.actualizarTiempo].
 *  - En todo momento pueden leer [ControladorJuego.estadoActual] para
 *    pintar la UI (temporizador, indicador de dirección, Frío/Tibio/Caliente,
 *    y el botón de reiniciar debe llamar [ControladorJuego.reiniciar]).
 */
object LogicaJuego {

    /** Bonificación de puntos si se encuentra al personaje con mucha precisión. */
    private const val BONO_PRECISION = 200

    /** Puntuación máxima posible antes de aplicar penalización por tiempo. */
    private const val PUNTUACION_BASE = 1000

    /** Puntos que se restan por cada segundo que tarda el jugador en encontrar al personaje. */
    private const val PENALIZACION_POR_SEGUNDO = 5

    /**
     * Genera una nueva dirección objetivo aleatoria en el rango [0, 360).
     * Representa la dirección donde queda "escondido" el personaje.
     */
    fun generarDireccionObjetivo(random: Random = Random.Default): Float {
        return random.nextFloat() * 360f
    }

    /**
     * Calcula la distancia angular más corta entre dos direcciones (en grados),
     * considerando que la brújula es circular (0 y 360 son el mismo punto).
     * El resultado siempre está en el rango [0, 180].
     *
     * Ejemplo: entre 350° y 10° la diferencia real es 20°, no 340°.
     */
    fun calcularDiferenciaAngular(direccionActual: Float, direccionObjetivo: Float): Float {
        val diferenciaCruda = abs(direccionActual - direccionObjetivo) % 360f
        return if (diferenciaCruda > 180f) 360f - diferenciaCruda else diferenciaCruda
    }

    /**
     * Determina el estado de temperatura (Frío/Tibio/Caliente) según qué tan
     * cerca está el jugador de la dirección objetivo, en relación con el
     * margen definido por el nivel de dificultad.
     *
     * Umbrales (relativos al margen de "encontrado" del nivel):
     *  - CALIENTE: diferencia <= margen * 2
     *  - TIBIO:    diferencia <= margen * 4
     *  - FRIO:     cualquier otro caso
     */
    fun calcularEstadoTemperatura(diferenciaAngular: Float, margenGrados: Float): EstadoTemperatura {
        return when {
            diferenciaAngular <= margenGrados * 2f -> EstadoTemperatura.CALIENTE
            diferenciaAngular <= margenGrados * 4f -> EstadoTemperatura.TIBIO
            else -> EstadoTemperatura.FRIO
        }
    }

    /**
     * Indica si, con la diferencia angular actual, el jugador ya encontró
     * al personaje (está dentro del margen permitido por el nivel).
     */
    fun seEncontroPersonaje(diferenciaAngular: Float, margenGrados: Float): Boolean {
        return diferenciaAngular <= margenGrados
    }

    /**
     * Calcula la puntuación final de una partida ganada.
     * Ver la fórmula documentada en [ResultadoPartida].
     */
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

/**
 * Controlador con estado que orquesta una partida completa. Mantiene el
 * [EstadoJuego] actual y expone funciones para que la UI/ViewModel lo
 * actualicen a medida que llegan datos del sensor y del temporizador.
 *
 * Esta clase NO usa coroutines, LiveData ni StateFlow a propósito: así se
 * mantiene 100% independiente de Android y fácil de probar. El equipo de
 * UI puede envolverla en un ViewModel con StateFlow si lo necesita, por
 * ejemplo:
 *
 *   class JuegoViewModel : ViewModel() {
 *       private val controlador = ControladorJuego(NivelDificultad.MEDIO)
 *       private val _estado = MutableStateFlow(controlador.estadoActual)
 *       val estado: StateFlow<EstadoJuego> = _estado
 *
 *       fun onNuevaOrientacion(grados: Float) {
 *           controlador.actualizarOrientacion(grados)
 *           _estado.value = controlador.estadoActual
 *       }
 *   }
 */
class ControladorJuego(
    nivelInicial: NivelDificultad = NivelDificultad.MEDIO,
    private val random: Random = Random.Default
) {

    private var nivelActual: NivelDificultad = nivelInicial

    /** Tiempo total transcurrido desde que inició la partida (para el cálculo del puntaje). */
    private var tiempoTranscurridoMs: Long = 0L

    var estadoActual: EstadoJuego = EstadoJuego.inicial(
        nivel = nivelInicial,
        direccionObjetivo = LogicaJuego.generarDireccionObjetivo(random)
    )
        private set

    /**
     * Inicia (o cambia de nivel e inicia) una partida nueva: genera una
     * nueva dirección objetivo aleatoria y reinicia el temporizador.
     */
    fun iniciarPartida(nivel: NivelDificultad = nivelActual): EstadoJuego {
        nivelActual = nivel
        tiempoTranscurridoMs = 0L
        val direccionObjetivo = LogicaJuego.generarDireccionObjetivo(random)
        estadoActual = EstadoJuego.inicial(nivel = nivel, direccionObjetivo = direccionObjetivo)
        return estadoActual
    }

    /**
     * Debe llamarse cada vez que el sensor (BrujulaSensor) entrega una
     * nueva lectura de orientación. Recalcula diferencia angular, estado
     * de temperatura y determina si el jugador ya ganó.
     *
     * No hace nada si la partida ya terminó (GANADO o PERDIDO).
     */
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

    /**
     * Debe llamarse periódicamente (por ejemplo cada 100-1000 ms) por el
     * temporizador de la UI/ViewModel, indicando cuánto tiempo transcurrió
     * desde la última llamada. Actualiza el tiempo restante y detecta la
     * derrota por tiempo agotado.
     *
     * @param deltaMs milisegundos transcurridos desde el último tick.
     */
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

    /**
     * Reinicia la partida con el mismo nivel de dificultad: nueva dirección
     * objetivo aleatoria y temporizador desde cero. El botón "Reiniciar"
     * de la UI debe llamar esta función.
     */
    fun reiniciar(): EstadoJuego = iniciarPartida(nivelActual)

    /**
     * Devuelve el resultado final resumido de la partida (útil para
     * mostrar una pantalla de resultado). Solo tiene sentido llamarlo
     * cuando la fase ya es GANADO o PERDIDO.
     */
    fun obtenerResultado(): ResultadoPartida {
        return ResultadoPartida(
            fase = estadoActual.fase,
            tiempoUtilizadoMs = tiempoTranscurridoMs,
            diferenciaAngularFinal = estadoActual.diferenciaAngular,
            puntuacion = estadoActual.puntuacion
        )
    }
}
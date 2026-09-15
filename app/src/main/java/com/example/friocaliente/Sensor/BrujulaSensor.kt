package com.example.friocaliente.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ============================================================================
 *  BRUJULA SENSOR — lee acelerómetro + magnetómetro y expone la orientación
 *  del teléfono como un ángulo en grados (0-360), listo para que
 *  Logica/ControladorJuego.actualizarOrientacion(Float) lo consuma.
 * ============================================================================
 *
 * Esta clase NO conoce nada de EstadoJuego, LogicaJuego ni Compose: solo
 * sabe leer sensores de Android y entregar un Float. Esto respeta la misma
 * filosofía del taller (capas separadas, cada una con una sola
 * responsabilidad) y hace que sea fácil de probar por separado.
 *
 * CÓMO LO USA EL RESTO DEL EQUIPO (ViewModel / GameScreen):
 *
 *   class JuegoViewModel(context: Context) : ViewModel() {
 *       private val controlador = ControladorJuego(NivelDificultad.MEDIO)
 *       private val brujula = BrujulaSensor(context)
 *
 *       private val _estado = MutableStateFlow(controlador.estadoActual)
 *       val estado: StateFlow<EstadoJuego> = _estado
 *
 *       fun iniciar() {
 *           brujula.iniciar()
 *           viewModelScope.launch {
 *               brujula.orientacionGrados.collect { grados ->
 *                   _estado.value = controlador.actualizarOrientacion(grados)
 *               }
 *           }
 *       }
 *
 *       fun detener() = brujula.detener()
 *   }
 *
 * En Compose:
 *   val estado by viewModel.estado.collectAsState()
 *   DisposableEffect(Unit) {
 *       viewModel.iniciar()
 *       onDispose { viewModel.detener() }
 *   }
 *
 * IMPORTANTE — CICLO DE VIDA:
 *   Siempre llamar [iniciar] cuando la pantalla de juego se vuelve visible
 *   y [detener] cuando deja de estarlo (onResume/onPause, o un
 *   DisposableEffect en Compose). Dejar el listener registrado sin
 *   necesidad gasta batería.
 */
class BrujulaSensor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val acelerometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    // Últimas lecturas crudas de cada sensor. Se necesitan los dos juntos
    // (acelerómetro + magnetómetro) para calcular la matriz de rotación.
    private val lecturaAcelerometro = FloatArray(3)
    private val lecturaMagnetometro = FloatArray(3)
    private var hayAcelerometro = false
    private var hayMagnetometro = false

    private val matrizRotacion = FloatArray(9)
    private val valoresOrientacion = FloatArray(3)

    // Valor suavizado actual, para no arrancar el filtro desde 0 en cada iniciar().
    private var azimutSuavizado: Float = 0f

    /** Qué tanto se "resiste" el filtro a cambios bruscos. Entre 0 y 1: más alto = más suave pero más lento. */
    private val factorSuavizado = 0.85f

    private val _orientacionGrados = MutableStateFlow(0f)

    /**
     * Orientación actual del teléfono en grados (0-360), ya suavizada.
     * 0° = Norte, 90° = Este, 180° = Sur, 270° = Oeste (estándar de Android).
     */
    val orientacionGrados: StateFlow<Float> = _orientacionGrados.asStateFlow()

    /** True si el dispositivo tiene los sensores necesarios para jugar. */
    val sensorDisponible: Boolean
        get() = acelerometro != null && magnetometro != null

    /**
     * Empieza a escuchar los sensores. Debe llamarse cuando la pantalla de
     * juego se vuelve visible (por ejemplo en un DisposableEffect o
     * en onResume de la Activity/Fragment).
     */
    fun iniciar() {
        acelerometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        magnetometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    /**
     * Deja de escuchar los sensores. Debe llamarse siempre que la pantalla
     * de juego deja de estar visible (onPause / onDispose), para no gastar
     * batería innecesariamente.
     */
    fun detener() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, lecturaAcelerometro, 0, lecturaAcelerometro.size)
                hayAcelerometro = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, lecturaMagnetometro, 0, lecturaMagnetometro.size)
                hayMagnetometro = true
            }
        }

        // Solo podemos calcular orientación cuando ya llegó al menos una
        // lectura de cada sensor.
        if (!hayAcelerometro || !hayMagnetometro) return

        val exito = SensorManager.getRotationMatrix(
            matrizRotacion,
            null,
            lecturaAcelerometro,
            lecturaMagnetometro
        )
        if (!exito) return

        SensorManager.getOrientation(matrizRotacion, valoresOrientacion)

        // valoresOrientacion[0] = azimut en radianes, rango (-PI, PI]
        val azimutRadianes = valoresOrientacion[0]
        val azimutGradosCrudo = normalizarA360(Math.toDegrees(azimutRadianes.toDouble()).toFloat())

        azimutSuavizado = suavizarAngulo(anteriorGrados = azimutSuavizado, nuevoGrados = azimutGradosCrudo)
        _orientacionGrados.value = azimutSuavizado
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se necesita reaccionar a cambios de precisión para este juego,
        // pero el método debe implementarse por el contrato de SensorEventListener.
    }

    /**
     * Lleva un ángulo cualquiera al rango [0, 360).
     */
    private fun normalizarA360(grados: Float): Float {
        val resultado = grados % 360f
        return if (resultado < 0f) resultado + 360f else resultado
    }

    /**
     * Filtro de paso bajo (low-pass) adaptado para ángulos circulares.
     * No se puede promediar 359° y 1° directamente (daría ~180°, mal),
     * así que primero se calcula el camino más corto entre ambos ángulos
     * y se avanza una fracción de ese camino.
     */
    private fun suavizarAngulo(anteriorGrados: Float, nuevoGrados: Float): Float {
        var diferencia = nuevoGrados - anteriorGrados
        // Llevar la diferencia al rango (-180, 180] para tomar el camino corto.
        diferencia = ((diferencia + 180f) % 360f + 360f) % 360f - 180f
        val avance = (1f - factorSuavizado) * diferencia
        return normalizarA360(anteriorGrados + avance)
    }
}
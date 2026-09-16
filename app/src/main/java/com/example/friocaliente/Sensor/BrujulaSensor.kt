package com.example.friocaliente.Sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class BrujulaSensor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val acelerometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val lecturaAcelerometro = FloatArray(3)
    private val lecturaMagnetometro = FloatArray(3)
    private var hayAcelerometro = false
    private var hayMagnetometro = false

    private val matrizRotacion = FloatArray(9)
    private val valoresOrientacion = FloatArray(3)

    // Valor suavizado actual, para no arrancar el filtro desde 0 en cada iniciar().
    private var azimutSuavizado: Float = 0f

    private val factorSuavizado = 0.85f

    private val _orientacionGrados = MutableStateFlow(0f)

    val orientacionGrados: StateFlow<Float> = _orientacionGrados.asStateFlow()

    val sensorDisponible: Boolean
        get() = acelerometro != null && magnetometro != null

    fun iniciar() {
        acelerometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        magnetometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

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

        if (!hayAcelerometro || !hayMagnetometro) return

        val exito = SensorManager.getRotationMatrix(
            matrizRotacion,
            null,
            lecturaAcelerometro,
            lecturaMagnetometro
        )
        if (!exito) return

        SensorManager.getOrientation(matrizRotacion, valoresOrientacion)

        val azimutRadianes = valoresOrientacion[0]
        val azimutGradosCrudo = normalizarA360(Math.toDegrees(azimutRadianes.toDouble()).toFloat())

        azimutSuavizado = suavizarAngulo(anteriorGrados = azimutSuavizado, nuevoGrados = azimutGradosCrudo)
        _orientacionGrados.value = azimutSuavizado
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun normalizarA360(grados: Float): Float {
        val resultado = grados % 360f
        return if (resultado < 0f) resultado + 360f else resultado
    }
    private fun suavizarAngulo(anteriorGrados: Float, nuevoGrados: Float): Float {
        var diferencia = nuevoGrados - anteriorGrados
        diferencia = ((diferencia + 180f) % 360f + 360f) % 360f - 180f
        val avance = (1f - factorSuavizado) * diferencia
        return normalizarA360(anteriorGrados + avance)
    }
}
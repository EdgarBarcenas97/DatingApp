package com.plcoding.emergency.data.platform

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.plcoding.emergency.domain.ShakeDetector
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

actual class PlatformShakeDetector(
    private val context: Context
) : ShakeDetector {

    private val shakeFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val sensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private var listener: SensorEventListener? = null
    private var lastShakeTime = 0L

    companion object {
        private const val SHAKE_THRESHOLD = 12.0f
        private const val SHAKE_COOLDOWN_MS = 1000L
    }

    override fun observeShake(): Flow<Unit> = shakeFlow

    override fun start() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                if (acceleration > SHAKE_THRESHOLD) {
                    val now = System.currentTimeMillis()
                    if (now - lastShakeTime > SHAKE_COOLDOWN_MS) {
                        lastShakeTime = now
                        shakeFlow.tryEmit(Unit)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun stop() {
        listener?.let { sensorManager.unregisterListener(it) }
        listener = null
    }
}

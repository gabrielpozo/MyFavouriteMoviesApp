package com.light.finder.common

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector(private val listener: Listener) : SensorEventListener {

    private var accelerationThreshold = DEFAULT_ACCELERATION_THRESHOLD

    private val queue = SampleQueue()

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null


    interface Listener {
        fun hearShake()
    }

    fun start(sensorManager: SensorManager): Boolean {

        if (accelerometer != null) {
            return true
        }

        accelerometer = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        if (accelerometer != null) {
            this.sensorManager = sensorManager
            sensorManager.registerListener(
                this, accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
        return accelerometer != null
    }

    fun stop() {
        if (accelerometer != null) {
            queue.clear()
            sensorManager?.unregisterListener(this, accelerometer)
            sensorManager = null
            accelerometer = null
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val accelerating = isAccelerating(event)
        val timestamp = event.timestamp
        queue.add(timestamp, accelerating)
        if (queue.isShaking) {
            queue.clear()
            listener.hearShake()
        }
    }

    private fun isAccelerating(event: SensorEvent): Boolean {
        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]
        val magnitudeSquared = (ax * ax + ay * ay + az * az).toDouble()
        return magnitudeSquared > accelerationThreshold * accelerationThreshold
    }


    internal class SampleQueue {

        private val pool = SamplePool()

        private var oldest: Sample? = null
        private var newest: Sample? = null
        private var sampleCount: Int = 0
        private var acceleratingCount: Int = 0

        val isShaking: Boolean
            get() = (newest != null
                    && oldest != null
                    && newest!!.timestamp - oldest!!.timestamp >= MIN_WINDOW_SIZE
                    && acceleratingCount >= (sampleCount shr 1) + (sampleCount shr 2))


        fun add(timestamp: Long, accelerating: Boolean) {
            purge(timestamp - MAX_WINDOW_SIZE)

            val added = pool.acquire()
            added.timestamp = timestamp
            added.accelerating = accelerating
            added.next = null
            if (newest != null) {
                newest?.next = added
            }
            newest = added
            if (oldest == null) {
                oldest = added
            }

            sampleCount++
            if (accelerating) {
                acceleratingCount++
            }
        }

        fun clear() {
            while (oldest != null) {
                val removed = oldest
                oldest = removed?.next
                removed?.let { pool.release(it) }
            }
            newest = null
            sampleCount = 0
            acceleratingCount = 0
        }

        fun purge(cutoff: Long) {
            while (sampleCount >= MIN_QUEUE_SIZE
                && oldest != null && cutoff - oldest?.timestamp!! > 0
            ) {
                val removed = oldest
                if (removed!!.accelerating) {
                    acceleratingCount--
                }
                sampleCount--

                oldest = removed.next
                if (oldest == null) {
                    newest = null
                }
                pool.release(removed)
            }
        }

        companion object {

            private val MAX_WINDOW_SIZE: Long = 500000000 // 0.5s
            private val MIN_WINDOW_SIZE = MAX_WINDOW_SIZE shr 1 // 0.25s
            private val MIN_QUEUE_SIZE = 4
        }
    }


    internal class Sample {
        var timestamp: Long = 0
        var accelerating: Boolean = false
        var next: Sample? = null
    }

    internal class SamplePool {
        private var head: Sample? = null

        fun acquire(): Sample {
            var acquired = head
            if (acquired == null) {
                acquired = Sample()
            } else {
                head = acquired.next
            }
            return acquired
        }

        fun release(sample: Sample) {
            sample.next = head
            head = sample
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        private const val SENSITIVITY_MEDIUM = 13
        private const val DEFAULT_ACCELERATION_THRESHOLD = SENSITIVITY_MEDIUM
    }
}
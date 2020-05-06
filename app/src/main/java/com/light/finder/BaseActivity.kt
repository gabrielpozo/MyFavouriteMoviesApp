package com.light.finder

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.common.ShakeDetector
import timber.log.Timber


open class BaseActivity : AppCompatActivity(), ShakeDetector.Listener {

    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val shakeDetector = ShakeDetector(this)
        shakeDetector.start(sensorManager)
    }

    override fun hearShake() {
        Timber.e("ege", "oh yeah shake me")
    }


    override fun onDestroy() {
        if(this::shakeDetector.isInitialized) {
            shakeDetector.stop()
        }
        super.onDestroy()
    }
}
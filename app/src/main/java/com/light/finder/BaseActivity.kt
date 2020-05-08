package com.light.finder

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.common.ShakeDetector
import com.light.finder.di.modules.global.HardwareModule
import timber.log.Timber


abstract class BaseActivity : AppCompatActivity(), ShakeDetector.Listener {

    private lateinit var shakeDetector: ShakeDetector
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hardwareComponents = (application as SignifyApp).applicationComponent.plus(
            HardwareModule(this)
        )
        shakeDetector = hardwareComponents.shakeDetector

        setShakeDetector()
    }

    private fun setShakeDetector() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector.start(sensorManager)
    }

    override fun hearShake() {
        Timber.e("ege oh yeah shake me")
    }

    override fun onDestroy() {
        if(this::shakeDetector.isInitialized) {
            shakeDetector.stop()
        }
        super.onDestroy()
    }
}
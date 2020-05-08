package com.light.finder

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.common.ShakeDetector
import com.light.finder.di.modules.global.HardwareModule
import com.light.finder.extensions.startActivity
import timber.log.Timber


abstract class BaseLightFinderActivity : AppCompatActivity(), ShakeDetector.Listener {

    private lateinit var shakeDetector: ShakeDetector
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hardwareComponents = (application as SignifyApp).applicationComponent.plus(
            HardwareModule(this)
        )
        shakeDetector = hardwareComponents.shakeDetector

    }

    override fun onResume() {
        super.onResume()
        setShakeDetector()
    }

    private fun setShakeDetector() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector.start(sensorManager)
    }

    override fun hearShake() {
        stopListening()
        startActivity<UsabillaActivity> {
            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    fun stopListening() {
        if(::shakeDetector.isInitialized) {
            shakeDetector.stop()
        }
    }

    override fun onPause() {
        super.onPause()

        stopListening()
    }
}
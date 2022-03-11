package de.digitaldealer.cardsplease.ui.base

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.chuckerteam.chucker.api.Chucker
import com.squareup.seismic.ShakeDetector
import de.digitaldealer.cardsplease.BuildConfig

open class BaseActivity : ComponentActivity(), ShakeDetector.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            (getSystemService(SENSOR_SERVICE) as SensorManager?)?.let { sensorManager ->
                ShakeDetector(this).also { shakeDetector -> shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME) }
            }
        }
    }

    override fun hearShake() = startActivity(Chucker.getLaunchIntent(this))
}

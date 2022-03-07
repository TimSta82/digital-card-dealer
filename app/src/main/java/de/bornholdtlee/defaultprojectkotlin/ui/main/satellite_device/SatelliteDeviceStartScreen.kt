package de.bornholdtlee.defaultprojectkotlin.ui.main.satellite_device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SatelliteDeviceStartScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = "Satellite device")
        }
    }
}
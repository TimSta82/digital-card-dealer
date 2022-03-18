package de.digitaldealer.cardsplease.ui.main.satellite_device

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.main.satellite_device.qr_scanner.QrScannerScreen

@Composable
fun SatelliteDeviceStartScreen(modifier: Modifier = Modifier, navController: NavController) {
    QrScannerScreen(navController)
}

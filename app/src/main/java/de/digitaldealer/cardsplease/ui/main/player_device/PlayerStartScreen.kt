package de.digitaldealer.cardsplease.ui.main.player_device

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.main.player_device.qr_scanner.QrScannerScreen

@Composable
fun PlayerStartScreen(modifier: Modifier = Modifier, navController: NavController) {
    QrScannerScreen(navController)
}

package de.digitaldealer.cardsplease.ui.main.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.NavigationRoutes

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(onClick = { navController.navigate(route = NavigationRoutes.DEALER_DEVICE_SCREEN) }) {
                Text(text = "Neues Spiel starten")
            }
            Button(onClick = { navController.navigate(route = NavigationRoutes.SATELLITE_DEVICE_START_SCREEN) }) {
                Text(text = "An einem Spiel teilnehmen")
            }
        }
    }
}
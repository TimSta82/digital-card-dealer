package de.bornholdtlee.defaultprojectkotlin.ui.main.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.bornholdtlee.defaultprojectkotlin.ui.NavigationRoutes

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(onClick = { navController.navigate(route = NavigationRoutes.CENTRAL_DEVICE_START_SCREEN) }) {
                Text(text = "Neues Spiel starten")
            }
            Button(onClick = { navController.navigate(route = NavigationRoutes.SATELLITE_DEVICE_START_SCREEN) }) {
                Text(text = "An einem Spiel teilnehmen")
            }
        }
    }
}
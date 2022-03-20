package de.digitaldealer.cardsplease.ui.main.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.main.composables.EntryCard
import de.digitaldealer.cardsplease.ui.main.composables.EntryType
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimary)),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.padding(four_GU)) {
            EntryCard(entryType = EntryType.DEALER, onClick = { navController.navigate(route = NavigationRoutes.DEALER_DEVICE_START_SCREEN) })
            Spacer(modifier = Modifier.height(two_GU))
            EntryCard(entryType = EntryType.PLAYER, onClick = { navController.navigate(route = NavigationRoutes.PLAYER_DEVICE_START_SCREEN) })
        }
    }
}
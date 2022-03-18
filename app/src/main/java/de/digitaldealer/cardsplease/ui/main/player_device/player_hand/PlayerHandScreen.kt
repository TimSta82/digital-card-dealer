package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.PokerCard
import de.digitaldealer.cardsplease.ui.main.dealer_device.Hand

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerHandViewModel = viewModel()

    val deckId by viewModel.deckId.collectAsStateLifecycleAware()
    val nickName by viewModel.nickname.collectAsStateLifecycleAware()

    val hand: Hand by viewModel.currentHand.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = deckId, key2 = nickName) {
        if (deckId != "-1" && nickName != "-1") viewModel.onStart()
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimaryDark)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "SpielId: $deckId")
            Text(text = "$nickName ihm seine Hand")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PokerCard(card = hand.one)
                Spacer(modifier = Modifier.width(16.dp))
                PokerCard(card = hand.two)
            }
        }
    }
}
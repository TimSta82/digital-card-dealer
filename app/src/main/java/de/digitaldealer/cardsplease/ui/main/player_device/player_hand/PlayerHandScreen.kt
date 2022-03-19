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
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.HandCard

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerHandViewModel = viewModel()

    val player by viewModel.player.collectAsStateLifecycleAware()
    val hand by viewModel.currentHand.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = hand.one.code != "") {
        viewModel.onStart()
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

    HandContent(player = player, hand = hand)
}

@Composable
fun HandContent(
    modifier: Modifier = Modifier,
    player: Player,
    hand: Hand
) {
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
            Text(text = "SpielId: ${player.deckId}")
            Text(text = "${player.nickName} ihm seine Hand")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = hand.one.code)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = hand.two.code)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HandCard(card = hand.one)
                Spacer(modifier = Modifier.width(16.dp))
                HandCard(card = hand.two)
            }
        }
    }
}

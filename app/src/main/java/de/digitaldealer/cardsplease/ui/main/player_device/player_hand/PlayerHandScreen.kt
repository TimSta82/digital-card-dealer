@file:OptIn(ExperimentalMaterialApi::class)

package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.CardFace
import de.digitaldealer.cardsplease.ui.main.composables.FlipCard
import de.digitaldealer.cardsplease.ui.main.composables.PlayerLeaveTableDialog
import de.digitaldealer.cardsplease.ui.main.composables.RotationAxis

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerHandViewModel = viewModel()

    val showPlayerLeaveDialog = remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val player by viewModel.player.collectAsStateLifecycleAware()
    val hand by viewModel.currentHand.collectAsStateLifecycleAware()
    val onLeaveTable by viewModel.onLeaveTable.observeAsState()

    LaunchedEffect(key1 = hand.one.code != "") {
        viewModel.onStart()
    }
    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }
    if (showPlayerLeaveDialog.value) PlayerLeaveTableDialog(onDismiss = { showPlayerLeaveDialog.value = false }, onDisconnectPlayer = viewModel::disconnectPlayer)

    if (onLeaveTable == true) {
        showPlayerLeaveDialog.value = false
        navController.navigate(route = NavigationRoutes.START_SCREEN)
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            HandBottomSheet(hand = hand)
        },
        sheetPeekHeight = 40.dp
    ) {
        HandContent(
            player = player,
            hand = hand,
            onDisconnectPlayer = { showPlayerLeaveDialog.value = true })
    }
}

@Composable
fun HandContent(
    modifier: Modifier = Modifier,
    player: Player,
    hand: Hand,
    onDisconnectPlayer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimaryDark)),
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                FloatingActionButton(onClick = onDisconnectPlayer) {
                    Icon(Icons.Filled.Delete, "")
                }
            }
            Text(text = "SpielId: ${player.deckId}")
            Text(text = "${player.nickName} ihm seine Hand")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                var cardOneState by remember {
                    mutableStateOf(CardFace.Front)
                }
                FlipCard(
                    card = hand.one,
                    cardFace = cardOneState,
                    onClick = {
                        cardOneState = it.next
                    },
                    axis = RotationAxis.AxisY,
                )
                Spacer(modifier = Modifier.width(16.dp))
                var cardTwoState by remember {
                    mutableStateOf(CardFace.Front)
                }
                FlipCard(
                    card = hand.two,
                    cardFace = cardTwoState,
                    onClick = {
                        cardTwoState = it.next
                    },
                    axis = RotationAxis.AxisY
                )
            }
        }
    }
}

@Composable
fun HandBottomSheet(modifier: Modifier = Modifier, hand: Hand) {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            CardFace(card = hand.one)
            Spacer(modifier = Modifier.width(8.dp))
            CardFace(card = hand.two)
        }
    }
}

//@Preview
//@Composable
//fun Preview_PlayerHandScreen(modifier: Modifier = Modifier) {
////    PlayerHandScreen()
//}

@Preview
@Composable
fun Preview_HandContent(modifier: Modifier = Modifier) {
    HandContent(player = Player(), hand = Hand(), onDisconnectPlayer = {})
}

@Preview
@Composable
fun Preview_HandBottomSheet(modifier: Modifier = Modifier) {
    HandBottomSheet(hand = Hand())
}

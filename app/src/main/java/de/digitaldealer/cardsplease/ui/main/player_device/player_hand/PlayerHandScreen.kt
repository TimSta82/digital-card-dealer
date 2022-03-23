@file:OptIn(ExperimentalMaterialApi::class)

package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.*
import de.digitaldealer.cardsplease.ui.theme.one_GU
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerHandViewModel = viewModel()

    val showPlayerLeaveDialog = remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val player by viewModel.player.collectAsStateLifecycleAware()
    val hand by viewModel.currentHand.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = hand.one.code != "") {
        viewModel.onStart()
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onLeaveTable.collectLatest {
            showPlayerLeaveDialog.value = false
            navController.navigate(route = NavigationRoutes.START_SCREEN)
        }
    }

    if (showPlayerLeaveDialog.value) PlayerLeaveTableDialog(onDismiss = { showPlayerLeaveDialog.value = false }, onDisconnectPlayer = viewModel::disconnectPlayer)

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            HandBottomSheet(hand = hand)
        },
        sheetPeekHeight = if (hand.isValid()) 40.dp else 0.dp,
        sheetShape = MaterialTheme.shapes.small
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(one_GU),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    CustomText(text = "Tisch: ${player.tableName}", textAlign = TextAlign.Start)
                    CustomText(text = "SpielId: ${player.deckId}", textAlign = TextAlign.Start)
                    CustomText(text = "${player.nickName} ihm seine Hand", textAlign = TextAlign.Start)
                }
                FloatingActionButton(onClick = onDisconnectPlayer) {
                    Icon(Icons.Filled.ExitToApp, "")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (hand.isValid()) {
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
            } else {
                CustomText(text = "Du hast noch keine Karten erhalten")
            }
        }
    }
}

@Composable
fun HandBottomSheet(modifier: Modifier = Modifier, hand: Hand) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.padding(one_GU)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            CardFace(card = hand.one)
            Spacer(modifier = Modifier.width(one_GU))
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

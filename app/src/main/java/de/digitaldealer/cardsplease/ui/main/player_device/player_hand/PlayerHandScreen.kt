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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.*
import de.digitaldealer.cardsplease.ui.main.start.bottomSheetShape
import de.digitaldealer.cardsplease.ui.theme.five_GU
import de.digitaldealer.cardsplease.ui.theme.half_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
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

    if (showPlayerLeaveDialog.value) SimpleDialog(
        title = "Willst du den Tisch wirklich verlassen?",
        buttonText = "Verlassen",
        onDismiss = { showPlayerLeaveDialog.value = false },
        onConfirmClicked = viewModel::disconnectPlayer
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            HandBottomSheet(hand = hand)
        },
        drawerGesturesEnabled = true,
        sheetPeekHeight = if (hand.isValid()) five_GU else 0.dp,
        sheetShape = bottomSheetShape()
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
        ConstraintLayout(
            modifier = Modifier
                .padding(one_GU)
                .fillMaxSize()
        ) {
            val (card, button, revealHand, text) = createRefs()
            EntryCard(
                modifier = Modifier
                    .padding(horizontal = two_GU)
                    .constrainAs(card) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(button.start)
                    },
                entryType = EntryType.PLAYER,
                content = {
                    Column(
                        modifier = Modifier.padding(one_GU)
                    ) {
                        CustomText(text = "Tisch: ${player.tableName}", textAlign = TextAlign.Start)
                        Spacer(Modifier.height(half_GU))
                        CustomText(text = "tableId: ${player.tableId}", textAlign = TextAlign.Start)
                        if (hand.round != 0) {
                            Spacer(Modifier.height(half_GU))
                            CustomText(text = "Runde: ${hand.round}", textAlign = TextAlign.Start)
                        }
                    }
                }, onClick = {})
            FloatingActionButton(
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(card.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(card.bottom)
                },
                onClick = onDisconnectPlayer
            ) {
                Icon(Icons.Filled.ExitToApp, "")
            }
            if (hand.isValid()) {
                Row(
                    modifier = Modifier
                        .constrainAs(revealHand) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
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
                    Spacer(modifier = Modifier.width(one_GU))
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
                CustomText(
                    modifier = Modifier
                        .constrainAs(revealHand) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    text = "Du hast noch keine Karten erhalten"
                )
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

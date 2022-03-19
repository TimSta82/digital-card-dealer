@file:OptIn(ExperimentalMaterialApi::class)

package de.digitaldealer.cardsplease.ui.main.player_device.player_hand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Hand
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.HandCard
import kotlinx.coroutines.launch

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier) {

    val viewModel: PlayerHandViewModel = viewModel()

//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val player by viewModel.player.collectAsStateLifecycleAware()
    val hand by viewModel.currentHand.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = hand.one.code != "") {
        viewModel.onStart()
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

//    BottomSheetScaffold(
//        sheetContent = {
//            HandBottomSheet(hand = hand)
//        },
//        scaffoldState = bottomSheetScaffoldState,
//    ) {
//        /* Add code later */
//    }

    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            HandBottomSheet(hand = hand)
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text(text = "TopAppBar")
//                    }
//                )
//            },
            bottomBar = {
                BottomAppBar(modifier = Modifier) {
                    IconButton(
                        onClick = {
                            scope.launch { bottomState.show() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                }
            },

            content = { innerPadding ->
                HandContent(player = player, hand = hand)
            }
        )
    }
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
        contentAlignment = Alignment.TopCenter
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

@Composable
fun HandBottomSheet(modifier: Modifier = Modifier, hand: Hand) {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            HandCard(card = hand.one)
            Spacer(modifier = Modifier.width(8.dp))
            HandCard(card = hand.two)
        }
    }
}

@Preview
@Composable
fun Preview_PlayerHandScreen(modifier: Modifier = Modifier) {
    PlayerHandScreen()
}

@Preview
@Composable
fun Preview_HandContent(modifier: Modifier = Modifier) {
    HandContent(player = Player(), hand = Hand())
}

@Preview
@Composable
fun Preview_HandBottomSheet(modifier: Modifier = Modifier) {
    HandBottomSheet(hand = Hand())
}
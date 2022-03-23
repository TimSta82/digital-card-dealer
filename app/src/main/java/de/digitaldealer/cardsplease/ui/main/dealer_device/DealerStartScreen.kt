package de.digitaldealer.cardsplease.ui.main.dealer_device

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.ui.main.composables.AddPlayerDialog
import de.digitaldealer.cardsplease.ui.main.composables.CardFace
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.main.composables.SimpleDialog
import de.digitaldealer.cardsplease.ui.theme.fourteen_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DealerStartScreen(modifier: Modifier = Modifier) {
    val viewModel: DealerViewModel = viewModel()

    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val addPlayerDeckId by viewModel.addPlayerWithDeckId.observeAsState()
    val deck by viewModel.deck.observeAsState()
    val playerCountError by viewModel.onPlayerCountError.observeAsState()

    if (addPlayerDeckId != null) AddPlayerDialog(viewModel = viewModel, deckId = addPlayerDeckId?.deckId ?: "", tableName = addPlayerDeckId?.tableName ?: "")

    val showDeleteGameDialog = remember { mutableStateOf(false) }

    if (showDeleteGameDialog.value) SimpleDialog(
        title = "Wollt ihr das Spiel wirklich beenden?",
        buttonText = "Spiel beenden",
        onDismiss = { showDeleteGameDialog.value = false },
        onConfirmClicked = viewModel::quitTable
    )

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.onDealingCardsToPlayersAccomplished.collectLatest {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Karten wurden ausgeteilt",
                )
            }
        }

        launch {
            viewModel.onShowErrorMessage.collectLatest {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = it,
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.onPlaySound.collectLatest {
                MediaPlayer.create(context, R.raw.deal_cards_sound).start()
            }
        }
    }

    LaunchedEffect(key1 = deck?.deckId) {
        deck?.let {
            viewModel.onStart(it)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState // attaching `scaffoldState` to the `Scaffold`
    ) {
        DealerContent(viewModel, onDismissQuitDialog = { showDeleteGameDialog.value = true })
    }
}

@Composable
fun DealerContent(viewModel: DealerViewModel, onDismissQuitDialog: () -> Unit) {
    val deck by viewModel.deck.observeAsState()
    val gamePhase by viewModel.gamePhase.observeAsState()
    val joinedPlayers by viewModel.joinedPlayers.observeAsState()
    val flop by viewModel.flop.observeAsState(emptyList())
    val turn by viewModel.turn.observeAsState(emptyList())
    val river by viewModel.river.observeAsState(emptyList())

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.dealer_background))
            .padding(two_GU)
    ) {
        val (infoButton, tableInfo, quitButton, board, dealerButton, playerInfo, addPlayerButton, resetButton) = createRefs()
        FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(infoButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }) {
            Icon(Icons.Filled.Info, contentDescription = "")
        }
        CustomText(text = "Tisch: ${deck?.tableName}", modifier = Modifier.constrainAs(tableInfo) {
            top.linkTo(parent.top)
            start.linkTo(infoButton.end)
            end.linkTo(quitButton.start)
        })
        FloatingActionButton(onClick = onDismissQuitDialog, modifier = Modifier.constrainAs(quitButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "")
        }
        Row(
            /** Board */
            modifier = Modifier.constrainAs(board) {
                top.linkTo(tableInfo.bottom, margin = two_GU)
                start.linkTo(parent.start)
                end.linkTo(dealerButton.start)
                bottom.linkTo(playerInfo.top, margin = two_GU)
            }, horizontalArrangement = Arrangement.Start
        ) {
            Flop(flop = flop)
            Spacer(modifier = Modifier.width(one_GU))
            Turn(turn = turn)
            Spacer(modifier = Modifier.width(one_GU))
            River(river = river)
        }
        FloatingActionButton(
            onClick = { gamePhase?.let { phase -> viewModel.deal(gamePhase = phase) } },
            modifier = Modifier
                .size(fourteen_GU)
                .constrainAs(dealerButton) {
                    top.linkTo(quitButton.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(addPlayerButton.top)
                }) {
            Text(text = gamePhase?.buttonText ?: "")
        }
        if (gamePhase != GamePhase.DEAL) {
            FloatingActionButton(
                onClick = { viewModel.reset() },
                modifier = Modifier
                    .padding(top = two_GU)
                    .constrainAs(resetButton) {
                        top.linkTo(dealerButton.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = "Reset")
            }
        }
        joinedPlayers.let { players ->
            CustomText(text = "Spieler: ${players?.count()}", modifier = Modifier.constrainAs(playerInfo) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            })
            if (players == null || players.size < 10) {
                FloatingActionButton(onClick = { viewModel.addPlayer() }, modifier = Modifier.constrainAs(addPlayerButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "")
                }
            }
        }
    }
}


@Composable
fun Flop(flop: List<Card?>) {
    LazyRow {
        itemsIndexed(flop) { index, card ->
            card?.let {
                CardFace(card = card)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun Turn(turn: List<Card?>) {
    LazyRow {
        itemsIndexed(turn) { index, card ->
            card?.let {
                CardFace(card = card)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Composable
fun River(river: List<Card?>) {
    LazyRow {
        itemsIndexed(river) { index, card ->
            card?.let {
                CardFace(card = card)
            }
        }
    }
}

@Preview
@Composable
fun Preview_DealerStartScreen(modifier: Modifier = Modifier) {
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
    DealerStartScreen()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview
@Composable
fun Preview_DealerContent(modifier: Modifier = Modifier) {
    val viewModel: DealerViewModel = viewModel()
    DealerContent(viewModel, onDismissQuitDialog = {})
}

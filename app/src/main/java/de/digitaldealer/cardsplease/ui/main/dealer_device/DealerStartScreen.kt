package de.digitaldealer.cardsplease.ui.main.dealer_device

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
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
fun DealerStartScreen(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: DealerViewModel = viewModel()

    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
    val context = LocalContext.current

    val onOpenAddPlayerDialog by viewModel.onOpenAddPlayerDialog.observeAsState()
    val table by viewModel.table.collectAsStateLifecycleAware()

    if (onOpenAddPlayerDialog != null) AddPlayerDialog(viewModel = viewModel, tableId = onOpenAddPlayerDialog!!.tableId, tableName = onOpenAddPlayerDialog!!.tableName)

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
        launch {
            viewModel.onNavigateBack.collectLatest {
                navController?.navigate(route = NavigationRoutes.START_SCREEN)
            }
        }
        launch {
            viewModel.onPlaySound.collectLatest {
                MediaPlayer.create(context, R.raw.deal_cards_sound).start()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onStart()
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
    val table by viewModel.table.collectAsStateLifecycleAware()
    val gamePhase by viewModel.gamePhase.observeAsState()
    val joinedPlayers by viewModel.joinedPlayers.observeAsState()
    val flop by viewModel.flop.observeAsState(emptyList())
    val turn by viewModel.turn.observeAsState(emptyList())
    val river by viewModel.river.observeAsState(emptyList())
    val round by viewModel.round.collectAsStateLifecycleAware()

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
        CustomText(text = "Tisch: ${table.tableName} - Runde: $round", modifier = Modifier.constrainAs(tableInfo) {
            top.linkTo(infoButton.top)
            bottom.linkTo(infoButton.bottom)
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
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(board) {
                    top.linkTo(tableInfo.bottom, margin = two_GU)
                    start.linkTo(parent.start)
                    if (joinedPlayers?.size ?: 0 > 1) end.linkTo(dealerButton.start) else end.linkTo(parent.end)
                    bottom.linkTo(playerInfo.top, margin = two_GU)
                }, horizontalArrangement = Arrangement.Start
        ) {
            ShowBoardMessage(joinedPlayers)
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
            ExtendedFloatingActionButton(
                onClick = { viewModel.reset() },
                modifier = Modifier
                    .padding(top = two_GU)
                    .constrainAs(resetButton) {
                        top.linkTo(dealerButton.bottom)
                        end.linkTo(parent.end)
                    },
                text = {
                    Text(text = "Reset")
                }
            )
        }
        joinedPlayers.let { players ->
            CustomText(text = "Spieler: ${players?.count()}", modifier = Modifier.constrainAs(playerInfo) {
                start.linkTo(parent.start)
                top.linkTo(addPlayerButton.top)
                bottom.linkTo(addPlayerButton.bottom)
            })
            if (players == null || players.size < 10) {
                ExtendedFloatingActionButton(onClick = { viewModel.addPlayer() }, modifier = Modifier.constrainAs(addPlayerButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                    text = {
                        Text(text = "Spieler hinzufügen")
                    })
            }
        }
    }
}

@Composable
fun ShowBoardMessage(players: List<Player>?) {
    when (players?.size) {
        null, 0 -> CustomText(text = "Um spielen zu können, müssen mindestens 2 Spieler teilnehmen")
        1 -> CustomText(text = "Jetzt fehlt noch 1 Spieler")
        else -> {}
    }
}


@Composable
fun Flop(flop: List<Card?>) {
    Row {
        flop.forEach { card ->
            card?.let {
                CardFace(card = card, modifier = Modifier.weight(1.0f))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun Turn(turn: List<Card?>) {
    Row {
        turn.forEach { card ->
            card?.let {
                CardFace(card = card, modifier = Modifier.weight(1.0f))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Composable
fun River(river: List<Card?>) {
    Row {
        river.forEach { card ->
            card?.let {
                CardFace(card = card, modifier = Modifier.weight(1.0f))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int, navController: NavController) {
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
    DealerStartScreen(navController = navController)
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

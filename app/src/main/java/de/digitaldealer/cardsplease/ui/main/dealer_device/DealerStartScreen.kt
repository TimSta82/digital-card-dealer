@file:OptIn(ExperimentalMaterialApi::class)

package de.digitaldealer.cardsplease.ui.main.dealer_device

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.domain.model.DeckHelper
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.domain.model.PokerTable
import de.digitaldealer.cardsplease.extensions.second
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.*
import de.digitaldealer.cardsplease.ui.theme.fourteen_GU
import de.digitaldealer.cardsplease.ui.theme.half_GU
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

    val showDeleteGameDialog = remember { mutableStateOf(false) }
    val onShowAddPlayerDialog = remember { mutableStateOf(false) }
    val onShowPlayersDialog = remember { mutableStateOf(false) }
    val onShowHasInternetErrorDialog = remember { mutableStateOf(false) }
    val table by viewModel.table.collectAsStateLifecycleAware()
    val gamePhase by viewModel.gamePhase.collectAsStateLifecycleAware()
    val joinedPlayers by viewModel.joinedPlayers.collectAsStateLifecycleAware()
    val boardCards by viewModel.boardCards.collectAsStateLifecycleAware()
    val round by viewModel.round.collectAsStateLifecycleAware()

    if (showDeleteGameDialog.value) SimpleDialog(
        title = "Wollt ihr das Spiel wirklich beenden?",
        buttonText = "Spiel beenden",
        onDismiss = { showDeleteGameDialog.value = false },
        onConfirmClicked = viewModel::quitTable
    )

    if (onShowPlayersDialog.value) {
        SimpleDialog(
            players = joinedPlayers.map { player -> player.nickName },
            buttonText = "Achso",
            onDismiss = { onShowPlayersDialog.value = false },
            onConfirmClicked = { onShowPlayersDialog.value = false }
        )
    }

    if (onShowAddPlayerDialog.value) QrCodePlayerDialog(onDismiss = { onShowAddPlayerDialog.value = false }, tableId = table.tableId, tableName = table.tableName)
    if (!onShowHasInternetErrorDialog.value) SimpleDialog(
        title = "??berpr??fe deine Internetverbindung",
        buttonText = "Ok",
        onDismiss = { onShowHasInternetErrorDialog.value = false },
        onConfirmClicked = { onShowHasInternetErrorDialog.value = false }
    )

    LaunchedEffect(key1 = onShowHasInternetErrorDialog) {
        launch {
            viewModel.hasInternetAccess.collectLatest {
                onShowHasInternetErrorDialog.value = it
            }
        }
    }

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
        viewModel.onPlayerCountError.collectLatest {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it.errorMessage,
            )
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
        DealerContent(
            onShowPlayers = { onShowPlayersDialog.value = true },
            onDeal = { viewModel.deal(gamePhase = gamePhase) },
            onReset = viewModel::reset,
            onAddPlayer = { onShowAddPlayerDialog.value = true },
            onDismissQuitDialog = { showDeleteGameDialog.value = true },
            table = table,
            gamePhase = gamePhase,
            joinedPlayers = joinedPlayers,
            boardCards = boardCards,
            round = round
        )
    }
}

@Composable
fun DealerContent(
    onShowPlayers: () -> Unit,
    onDeal: () -> Unit,
    onReset: () -> Unit,
    onAddPlayer: () -> Unit,
    onDismissQuitDialog: () -> Unit,
    table: PokerTable,
    gamePhase: GamePhase?,
    joinedPlayers: List<Player>,
    boardCards: List<Card>,
    round: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(two_GU)
    ) {
        val (infoButton, tableInfo, quitButton, board, flopRow, turnRow, riverRow, dealerButton, playerInfo, addPlayerButton, resetButton, boardInfo) = createRefs()
        FloatingActionButton(
            onClick = onShowPlayers,
            modifier = Modifier.constrainAs(infoButton) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        ) {
            Icon(Icons.Filled.Info, contentDescription = "")
        }
        CustomText(
            text = "Tisch: ${table.tableName} - Runde: $round",
            modifier = Modifier.constrainAs(tableInfo) {
                top.linkTo(infoButton.top)
                bottom.linkTo(infoButton.bottom)
                start.linkTo(infoButton.end)
                end.linkTo(quitButton.start)
            }
        )
        FloatingActionButton(
            onClick = onDismissQuitDialog,
            modifier = Modifier.constrainAs(quitButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        ) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "")
        }
        if (isDefaultPlayer(joinedPlayers) || joinedPlayers.size <= 1) CustomText(
            modifier = Modifier.constrainAs(boardInfo) {
                top.linkTo(infoButton.bottom, margin = two_GU)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(addPlayerButton.top, margin = two_GU)
            },
            text = getBoardPlayerMessage(joinedPlayers)
        )
        BoardCards(
            /** Board */
            modifier = Modifier
                .padding(horizontal = half_GU)
                .constrainAs(board) {
                    top.linkTo(tableInfo.bottom, margin = two_GU)
                    start.linkTo(parent.start, margin = one_GU)
                    end.linkTo(dealerButton.start, margin = one_GU)
                    bottom.linkTo(playerInfo.top, margin = two_GU)
                    width = Dimension.preferredWrapContent
                },
            cards = boardCards
        )
        FloatingActionButton(
            onClick = onDeal,
            modifier = Modifier
                .size(fourteen_GU)
                .constrainAs(dealerButton) {
                    top.linkTo(quitButton.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(addPlayerButton.top)
                }
        ) {
            Text(text = gamePhase?.buttonText ?: "")
        }
        if (gamePhase != GamePhase.DEAL) {
            ExtendedFloatingActionButton(
                onClick = onReset,
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
        CustomText(
            text = "Spieler: ${if (isDefaultPlayer(players = joinedPlayers)) 0 else joinedPlayers.count()}",
            modifier = Modifier.constrainAs(playerInfo) {
                start.linkTo(parent.start)
                top.linkTo(addPlayerButton.top)
                bottom.linkTo(addPlayerButton.bottom)
            }
        )
        ExtendedFloatingActionButton(
            onClick = onAddPlayer,
            modifier = Modifier
                .alpha(if (joinedPlayers.size < 10) 1f else 0f)
                .constrainAs(addPlayerButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            text = {
                Text(text = "Spieler hinzuf??gen")
            }
        )
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 480)
@Composable
fun Preview_DealerContent() {
    DealerContent(
        onShowPlayers = {},
        onDeal = {},
        onReset = {},
        onAddPlayer = {},
        onDismissQuitDialog = { },
        table = PokerTable(),
        gamePhase = GamePhase.SHUFFLE,
        joinedPlayers = listOf(Player(), Player()),
        boardCards = listOf(
            DeckHelper.getClubsCard(),
            DeckHelper.getDiamondsCard(),
            DeckHelper.getClubsCard(),
            DeckHelper.getDiamondsCard(),
            DeckHelper.getClubsCard()
        ),
        round = 1
    )
}

fun isDefaultPlayer(players: List<Player>): Boolean {
    return players.any { player -> player.uuid == "" }
}

fun getBoardPlayerMessage(players: List<Player>): String {
    return when (if (isDefaultPlayer(players)) players.size - 1 else players.size) {
        0 -> "Um spielen zu k??nnen, m??ssen mindestens 2 Spieler teilnehmen"
        else -> "Jetzt fehlt noch 1 Spieler"
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoardCards(modifier: Modifier = Modifier, cards: List<Card>) {
    if (isValidAction(cards)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
        ) {
            Box(modifier.weight(1f)) {
                if (isValidCard(cards.first())) {
                    FlipCard(
                        cardFace = CardFace.Back,
                        card = cards.first(),
                        onClick = {}
                    )
                }
            }
            Box(modifier.weight(1f)) {
                if (isValidCard(cards.second())) {
                    FlipCard(
                        cardFace = CardFace.Back,
                        card = cards.second(),
                        onClick = {}
                    )
                }
            }
            Box(modifier.weight(1f)) {
                if (isValidCard(cards[2])) {
                    FlipCard(
                        cardFace = CardFace.Back,
                        card = cards[2],
                        onClick = {}
                    )
                }
            }
            Box(modifier.weight(1f)) {
                if (cards.size >= 4 && isValidCard(cards[3])) {
                    FlipCard(
                        cardFace = CardFace.Back,
                        card = cards[3],
                        onClick = {}
                    )
                }
            }
            Box(modifier.weight(1f)) {
                if (cards.size == 5 && isValidCard(cards[4])) {
                    FlipCard(
                        cardFace = CardFace.Back,
                        card = cards[4],
                        onClick = {}
                    )
                }
            }
        }
    }
}

fun isValidCard(card: Card): Boolean {
    return card.value != ""
}

fun isValidAction(cards: List<Card>): Boolean {
    return cards.none { card -> card.value == "" }
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

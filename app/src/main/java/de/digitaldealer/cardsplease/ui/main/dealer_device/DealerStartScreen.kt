package de.digitaldealer.cardsplease.ui.main.dealer_device

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.ui.main.composables.AddPlayerDialog
import de.digitaldealer.cardsplease.ui.main.composables.CardBack
import de.digitaldealer.cardsplease.ui.main.composables.CardFace

@Composable
fun DealerStartScreen(modifier: Modifier = Modifier) {
    val viewModel: DealerViewModel = viewModel()

    val flop by viewModel.flop.observeAsState(emptyList())
    val turn by viewModel.turn.observeAsState(emptyList())
    val river by viewModel.river.observeAsState(emptyList())
    val gamePhase by viewModel.gamePhase.observeAsState()
    val addPlayerDeckId by viewModel.addPlayerWithDeckId.observeAsState()
    val deck by viewModel.deck.observeAsState()
    val players by viewModel.joinedPlayers.observeAsState()
    val playerCountError by viewModel.onPlayerCountError.observeAsState()

    if (addPlayerDeckId != null) AddPlayerDialog(viewModel = viewModel, addPlayerDeckId = addPlayerDeckId)

    LaunchedEffect(key1 = deck?.deckId) {
        deck?.let {
            viewModel.onStart(it)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onStop() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (deck != null) Text(text = "SpielId: ${deck?.deckId}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(vertical = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    CardBack(elevation = 2.dp)
                    CardBack(elevation = 4.dp)
                    CardBack(elevation = 6.dp)
                    CardBack(elevation = 8.dp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                flop(flop)
                Spacer(modifier = Modifier.width(8.dp))
                turn(turn)
                Spacer(modifier = Modifier.width(8.dp))
                river(river)
                Spacer(modifier = Modifier.width(32.dp))
                Column {
                    gamePhase?.let { phase ->
                        FloatingActionButton(onClick = { viewModel.deal(gamePhase = phase) }) {
                            Text(text = phase.buttonText)
                        }
                    }
                    if (flop.any { card -> card != null }) {
                        Spacer(modifier = Modifier.height(16.dp))
                        FloatingActionButton(onClick = { viewModel.reset() }) {
                            Text(text = "Reset")
                        }
                    }
                }
                Spacer(modifier = Modifier.width(32.dp))
                FloatingActionButton(onClick = {
                    viewModel.addPlayer()
                }) {
                    Icon(Icons.Filled.Add, "")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Spieler: ${players?.count()}")
        }
    }
}

@Composable
fun flop(flop: List<Card?>) {
    Column {
        Text(text = "Flop", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            itemsIndexed(flop) { index, card ->
                card?.let {
                    CardFace(card = card)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun turn(turn: List<Card?>) {
    Column {
        Text(text = "Turn", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            itemsIndexed(turn) { index, card ->
                card?.let {
                    CardFace(card = card)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun river(river: List<Card?>) {
    Column {
        Text(text = "River", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            itemsIndexed(river) { index, card ->
                card?.let {
                    CardFace(card = card)
                }
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
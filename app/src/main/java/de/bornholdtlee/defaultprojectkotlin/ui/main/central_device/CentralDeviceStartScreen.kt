package de.bornholdtlee.defaultprojectkotlin.ui.main.central_device

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card
import de.bornholdtlee.defaultprojectkotlin.ui.main.composables.PokerCard

@Composable
fun CentralDeviceStartScreen(modifier: Modifier = Modifier) {
    val viewModel: CentralViewModel = viewModel()

    val flop by viewModel.flop.observeAsState(emptyList())
    val turn by viewModel.turn.observeAsState(emptyList())
    val river by viewModel.river.observeAsState(emptyList())
    val gamePhase by viewModel.gamePhase.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                PokerCard(card = null, elevation = 2.dp)
                PokerCard(card = null, elevation = 4.dp)
                PokerCard(card = null, elevation = 6.dp)
                PokerCard(card = null, elevation = 8.dp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            flop(flop)
            Spacer(modifier = Modifier.width(8.dp))
            turn(turn)
            Spacer(modifier = Modifier.width(8.dp))
            river(river)
            Spacer(modifier = Modifier.width(32.dp))
            gamePhase?.let { phase ->
                FloatingActionButton(onClick = { viewModel.deal(gamePhase = phase) }) {
                    Text(text = phase.buttonText)
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            FloatingActionButton(onClick = { viewModel.addPlayer() }) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

@Composable
fun flop(flop: List<Card?>) {
    LazyRow {
        itemsIndexed(flop) { index, card ->
            card?.let {
                PokerCard(card = card)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun turn(turn: List<Card?>) {
    LazyRow {
        itemsIndexed(turn) { index, card ->
            card?.let {
                PokerCard(card = card)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun river(river: List<Card?>) {
    LazyRow {
        itemsIndexed(river) { index, card ->
            card?.let {
                PokerCard(card = card)
            }
        }
    }
}

@Preview
@Composable
fun PreviewCentralDeviceScreen(modifier: Modifier = Modifier) {
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
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
    CentralDeviceStartScreen()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
package de.bornholdtlee.defaultprojectkotlin.ui.main.central_device

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card
import de.bornholdtlee.defaultprojectkotlin.ui.main.composables.PokerCard
import net.glxn.qrgen.android.QRCode

@Composable
fun CentralDeviceStartScreen(modifier: Modifier = Modifier) {
    val viewModel: CentralViewModel = viewModel()

    val flop by viewModel.flop.observeAsState(emptyList())
    val turn by viewModel.turn.observeAsState(emptyList())
    val river by viewModel.river.observeAsState(emptyList())
    val gamePhase by viewModel.gamePhase.observeAsState()
    val addPlayerDeckId by viewModel.addPlayerWithDeckId.observeAsState()

    if (addPlayerDeckId != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resetPlayerDeckId()
            },
            title = {
                Text(text = "Scanne mal den Code")
            },
            text = {
                addPlayerDeckId?.let {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            bitmap = getQrCodeAsBitmap(deckId = it),
                            contentDescription = ""
                        )
                    }
                }

            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.resetPlayerDeckId() }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }

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
//                val size = Point()
//                requireActivity().windowManager.defaultDisplay.getSize(size)
//                val bitmap = QRCode.from(appUserId).withSize(size.x, size.x).withColor(requireContext().getColor(R.color.primary), requireContext().getColor(R.color.foreground)).bitmap()
//                binding.exportQrCodeIv.setImageBitmap(bitmap)
//                getDimensionsForQrQode()
            }) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

@Composable
private fun getQrCodeAsBitmap(deckId: String): ImageBitmap {
    val bitmap = QRCode.from(deckId).withSize(300, 300).bitmap().asImageBitmap()
    return bitmap
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
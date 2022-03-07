package de.bornholdtlee.defaultprojectkotlin.ui.main.central_device

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.ui.main.composables.PokerCardFrontSide

@Composable
fun CentralDeviceStartScreen(modifier: Modifier = Modifier) {
    val viewModel: CentralViewModel = viewModel()

    val cards by viewModel.cards.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 64.dp)
        ) {
            PokerCardFrontSide(card = null)
            Spacer(modifier = Modifier.width(8.dp))
            LazyRow {
                itemsIndexed(cards) { index, card ->
                    card?.let {
                        PokerCardFrontSide(card = card)
                        Spacer(modifier = Modifier.width(8.dp))
//                        Card(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) {
//                            Box(modifier = Modifier.padding(40.dp)) {
//                                Text(modifier = Modifier.padding(all = 40.dp), text = card.code)
//                            }
//                        }
                    }
                }
            }
        }
    }
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
package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    title: String? = "",
    players: List<String>? = emptyList(),
    buttonText: String,
    onDismiss: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    AlertDialog(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        onDismissRequest = onDismiss,
        text = {
            if (players.isNullOrEmpty().not()) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .scrollable(scrollState, orientation = Orientation.Vertical),
                    horizontalAlignment = Alignment.Start,
                ) {
                    CustomText(text = "Teilnehmende Spieler:")
                    players?.forEach { player -> CustomText(text = player) }
                }
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomText(text = title ?: "")
                }
            }
        },
        confirmButton = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = two_GU),
                horizontalArrangement = Arrangement.Center
            ) {
                TriggerButton(
                    onClick = onConfirmClicked,
                    text = buttonText
                )
            }
        }
    )
}

@Composable
@Preview
fun Preview_SimpleDialog(modifier: Modifier = Modifier) {
    SimpleDialog(title = "Testy McTestface", buttonText = "OK", onDismiss = {}, onConfirmClicked = {})
}

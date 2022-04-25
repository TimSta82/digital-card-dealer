package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    title: String,
    buttonText: String,
    onDismiss: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        onDismissRequest = onDismiss,
        text = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomText(text = title)
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

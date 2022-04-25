package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    title: String,
    buttonText: String,
    onDismiss: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(text = title)
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TriggerButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onConfirmClicked,
                    text = buttonText
                )
            }
        }
    )
}

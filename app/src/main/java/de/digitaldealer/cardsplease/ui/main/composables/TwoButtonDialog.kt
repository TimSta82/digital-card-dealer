package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.digitaldealer.cardsplease.ui.theme.one_GU

@Composable
fun TwoButtonDialog(
    modifier: Modifier = Modifier,
    text: String,
    confirmButtonText: String,
    declineButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDecline: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            CustomText(text = text)
        },
        buttons = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TriggerButton(
                    onClick = onConfirm,
                    text = confirmButtonText
                )
                Spacer(modifier = Modifier.height(one_GU))
                TriggerButton(
                    onClick = onDecline,
                    text = declineButtonText
                )
            }
        }
    )
}

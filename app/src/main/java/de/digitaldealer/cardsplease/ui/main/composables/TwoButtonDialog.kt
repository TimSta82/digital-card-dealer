package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

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
        modifier = modifier,
        onDismissRequest = onDismiss,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        text = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomText(text = text)
            }
        },
        confirmButton = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = two_GU),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TriggerButton(
                    onClick = onConfirm,
                    text = confirmButtonText
                )
                Spacer(modifier = modifier.height(one_GU))
                TriggerButton(
                    onClick = onDecline,
                    text = declineButtonText
                )
            }
        }
    )
}

@Composable
@Preview
fun Preview_TwoButtonDialog(modifier: Modifier = Modifier) {
    TwoButtonDialog(text = "testy mcTestface", confirmButtonText = "Bestätigen", declineButtonText = "Abbrechen", onDismiss = { /*TODO*/ }, onConfirm = { /*TODO*/ }) {
    }
}

/**
 *
 * AlertDialog(
modifier = modifier,
onDismissRequest = onDismiss,
text = {
CustomText(text = text)
},
confirmButton = {},
buttons = {
Column(
modifier = modifier,
horizontalAlignment = Alignment.CenterHorizontally,
verticalArrangement = Arrangement.SpaceAround
) {
TriggerButton(
onClick = onConfirm,
text = confirmButtonText
)
Spacer(modifier = modifier.height(one_GU))
TriggerButton(
onClick = onDecline,
text = declineButtonText
)
}
}
)
 *
 */
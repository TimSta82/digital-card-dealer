package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.digitaldealer.cardsplease.ui.theme.White

@Composable
fun TriggerButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = White
        )
    }
}

@Composable
@Preview
private fun Preview_TriggerButton() {
    TriggerButton(
        text = "Test",
        onClick = {}
    )
}

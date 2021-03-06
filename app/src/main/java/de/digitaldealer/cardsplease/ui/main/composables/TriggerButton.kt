package de.digitaldealer.cardsplease.ui.main.composables

import android.content.res.Configuration
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TriggerButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
//        colors = buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
    ) {
        CustomText(text = text)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun Preview_TriggerButton() {
    TriggerButton(
        text = "Dark Mode",
        onClick = {}
    )
}

@Composable
@Preview
private fun Preview_TriggerButton2() {
    TriggerButton(
        text = "Light Mode",
        onClick = {}
    )
}

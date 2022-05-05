package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign
    )
}

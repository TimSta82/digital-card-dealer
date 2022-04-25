package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import de.digitaldealer.cardsplease.R

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            color = colorResource(id = R.color.white),
        ),
        textAlign = textAlign
    )
}

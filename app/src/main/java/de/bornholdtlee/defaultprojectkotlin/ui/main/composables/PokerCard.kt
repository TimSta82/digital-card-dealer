package de.bornholdtlee.defaultprojectkotlin.ui.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card

@Composable
fun PokerCardFrontSide(
    modifier: Modifier = Modifier,
    card: Card?
) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 40.dp)) {
//            if (isHidden) {
            if (card == null) {
                Image(Icons.Filled.Api, contentDescription = "")
            } else {
                Text(text = card.code)
            }
        }
    }
}

package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.ui.util.CardUtils

@Composable
fun PokerCard(
    modifier: Modifier = Modifier,
    card: Card?,
    elevation: Dp? = 4.dp,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        backgroundColor = if (card == null) Color.DarkGray else Color.White,
        elevation = elevation ?: 4.dp,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            if (card == null) {
                Image(Icons.Filled.Api, contentDescription = "")
            } else {
                Column {
                    Image(painter = painterResource(id = CardUtils.getSuitIcon(card.suit)), contentDescription = "")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = card.value, color = colorResource(id = CardUtils.getSuitColor(card.suit)), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun HandCard(
    modifier: Modifier = Modifier,
    card: Card,
    elevation: Dp? = 4.dp,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        backgroundColor = if (card == null) Color.DarkGray else Color.White,
        elevation = elevation ?: 8.dp,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 64.dp)
                .defaultMinSize(minHeight = 80.dp, minWidth = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Image(painter = painterResource(id = CardUtils.getSuitIcon(card.suit)), contentDescription = "")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = card.value, color = colorResource(id = CardUtils.getSuitColor(card.suit)), textAlign = TextAlign.Center)
            }
        }
    }
}
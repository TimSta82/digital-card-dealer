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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.bornholdtlee.defaultprojectkotlin.domain.model.Card

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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 40.dp)
        ) {
            if (card == null) {
                Image(Icons.Filled.Api, contentDescription = "")
            } else {
//                Image(
//                    painter = rememberImagePainter(
//                        data = card.image,
//                        builder = {
//                            transformations(CircleCropTransformation())
//                        }
//                    ),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                )
                Text(text = card.code, color = Color.Black)
            }
        }
    }
}

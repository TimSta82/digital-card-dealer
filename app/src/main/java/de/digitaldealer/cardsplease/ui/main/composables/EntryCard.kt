package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.theme.*

@Composable
fun EntryCard(
    modifier: Modifier = Modifier,
    entryType: EntryType,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(one_GU),
        modifier = Modifier
            .clip(RoundedCornerShape(one_GU))
            .clickable { onClick() },
        backgroundColor = colorResource(id = if (entryType == EntryType.DEALER) R.color.dealer_background else R.color.player_background),
        elevation = two_GU,
        border = BorderStroke(2.dp, color = Color.Black)
    ) {
        content()
    }
}

@Composable
fun EntryContent(modifier: Modifier = Modifier, entryType: EntryType, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = four_GU)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (entryType == EntryType.DEALER) "Ein neues Spiel als Dealer starten" else "Als Spieler einem Spiel beitreten", style = TextStyle(color = Color.White))
        Spacer(modifier = Modifier.height(two_GU))
        Button(onClick = { onClick() }) {
            Text(text = if (entryType == EntryType.DEALER) "Starten" else "Beitreten")
        }
    }
}

enum class EntryType {
    PLAYER, DEALER
}
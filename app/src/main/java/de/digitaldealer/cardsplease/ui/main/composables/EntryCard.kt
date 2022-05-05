package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun EntryCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(one_GU),
        modifier = modifier
            .clip(RoundedCornerShape(one_GU))
            .clickable { onClick() },
        elevation = two_GU
    ) {
        content()
    }
}

@Composable
fun EntryContent(
    modifier: Modifier = Modifier,
    entryType: EntryType,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = four_GU)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(text = if (entryType == EntryType.DEALER) "Ein neues Spiel als Dealer starten" else "Als Spieler einem Spiel beitreten")
        Spacer(modifier = modifier.height(two_GU))
        TriggerButton(
            onClick = onClick,
            text = if (entryType == EntryType.DEALER) "Starten" else "Beitreten"
        )
    }
}

enum class EntryType {
    PLAYER, DEALER
}

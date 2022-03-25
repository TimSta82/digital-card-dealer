package de.digitaldealer.cardsplease.ui.main.start.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.digitaldealer.cardsplease.domain.model.DeckHelper

@Composable
fun ImprintScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Freunde treffen - jaaaa\nPokern... - cool\nmit echten Chips - klappert so schön\nKarten mischen und verteilen... - lame! Mach ich nicht!\n\nLass doch einfach Cards Please " +
            "nehmen!")
    }
}
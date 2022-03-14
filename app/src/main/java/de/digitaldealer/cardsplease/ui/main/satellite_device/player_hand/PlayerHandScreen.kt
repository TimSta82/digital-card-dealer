package de.digitaldealer.cardsplease.ui.main.satellite_device.player_hand

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PlayerHandScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerHandViewModel = viewModel()

    val deckId by viewModel.deckId.observeAsState()
    val nickName by viewModel.nickname.observeAsState()
    Column {
        Text(deckId?.let { it } ?: "")
        Text(nickName?.let { it } ?: "")
    }
}
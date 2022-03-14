package de.digitaldealer.cardsplease.ui.main.satellite_device.player_nick_name

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN

@Composable
fun PlayerNickNameScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: PlayerNickNameViewModel = viewModel()

    val deckId by viewModel.deckId.observeAsState()
    val player by viewModel.player.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Card(elevation = 8.dp) {
            Column {
                Text(text = deckId?.let { it } ?: "Hallo affe", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                NickNameInputContainer(viewModel = viewModel)
            }
        }
    }

    LaunchedEffect(key1 = player?.nickName) {
        navController.navigate(route = "$PLAYER_HAND_SCREEN/${player?.deckId}/${player?.nickName}")
    }
}

@Composable
fun NickNameInputContainer(viewModel: PlayerNickNameViewModel) {
    val nickName = remember { mutableStateOf(TextFieldValue()) }
    Text(text = "bitte nickname eingeben")
    TextField(value = nickName.value, onValueChange = { nickName.value = it })

    Button(onClick = { viewModel.submitToGame(nickName.value.text) }) {
        Text(text = "anmelden")
    }
}
package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN

@Composable
fun InsertNameScreen(modifier: Modifier = Modifier, navController: NavController?) {

    val viewModel: InsertNameViewModel = viewModel()

    val deckId by viewModel.deckId.observeAsState()
    val player by viewModel.player.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Card(elevation = 8.dp) {
            Column(
                modifier = Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = deckId?.let { it } ?: "Hallo affe", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                InsertNameTextFieldContainer(viewModel = viewModel)
            }
        }
    }

    player?.let {
        navController?.navigate(route = "$PLAYER_HAND_SCREEN/${player?.deckId}/${player?.nickName}")
    }
}

@Composable
fun InsertNameTextFieldContainer(viewModel: InsertNameViewModel) {
    val nickName = remember { mutableStateOf(TextFieldValue()) }
    Text(text = "bitte nickname eingeben")
    TextField(value = nickName.value, onValueChange = { nickName.value = it })

    Button(onClick = { viewModel.submitToGame(nickName.value.text) }) {
        Text(text = "anmelden")
    }
}

@Preview
@Composable
fun Preview_InsertNameScreen(modifier: Modifier = Modifier) {
    InsertNameScreen(navController = null)
}
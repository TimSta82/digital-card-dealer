package de.digitaldealer.cardsplease.ui.main.satellite_device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SatelliteDeviceStartScreen(modifier: Modifier = Modifier) {

    val viewModel: SatelliteViewModel = viewModel()


    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            val deckId = remember { mutableStateOf(TextFieldValue()) }
            val nickName = remember { mutableStateOf(TextFieldValue()) }
            Text(text = "Satellite device")
            Text(text = "bitte deckId eingeben")
            TextField(value = deckId.value, onValueChange = { deckId.value = it })
            Text(text = "bitte nickname eingeben")
            TextField(value = nickName.value, onValueChange = { nickName.value = it })

            Button(onClick = { viewModel.submitToGame(deckId.value.text, nickName.value.text) }) {
                Text(text = "anmelden")
            }
        }
    }
}
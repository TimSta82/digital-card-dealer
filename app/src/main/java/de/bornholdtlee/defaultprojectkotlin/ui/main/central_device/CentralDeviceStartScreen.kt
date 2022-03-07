package de.bornholdtlee.defaultprojectkotlin.ui.main.central_device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CentralDeviceStartScreen(modifier: Modifier = Modifier) {
    val viewModel: CentralViewModel = viewModel()

    val cards by viewModel.cards.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(cards) { index, card ->
                card?.let {
                    Text(text = card.code)
                }
            }
        }
    }
}
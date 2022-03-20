@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.theme.half_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun InsertNameScreen(modifier: Modifier = Modifier, navController: NavController?) {

    val viewModel: InsertNameViewModel = viewModel()

    val deckIdWithTableName by viewModel.deckIdWithTableName.observeAsState()
    val player by viewModel.player.observeAsState()
    val isLoading by viewModel.isLoading.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = player != null) {
        player?.let {
            val playerJson = Uri.encode(Gson().toJson(player))
            navController?.navigate(route = "$PLAYER_HAND_SCREEN/${playerJson}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimaryDark)),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
//            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(horizontal = two_GU, vertical = two_GU + half_GU))
//            }
        } else {
            Card(elevation = 8.dp) {
                Column(
                    modifier = Modifier.padding(top = 24.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    deckIdWithTableName?.let {
                        Text(text = "Tisch: ${it.second}", textAlign = TextAlign.Center)
                        Text(text = "SpielId: ${it.first}", textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    InsertNameTextFieldContainer(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun InsertNameTextFieldContainer(viewModel: InsertNameViewModel) {
    val nickName = remember { mutableStateOf(TextFieldValue()) }
    val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(text = "Gib mal deinen Namen ein")
    Spacer(modifier = Modifier.height(32.dp))
    TextField(
        value = nickName.value,
        onValueChange = { nickName.value = it },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            if (nickName.value.text.isNotBlank()) viewModel.submitToGame(nickName.value.text)
        }),
    )
    Button(onClick = { if (nickName.value.text.isNotBlank()) viewModel.submitToGame(nickName.value.text) }) {
        Text(text = "anmelden")
    }
}

@Preview
@Composable
fun Preview_InsertNameScreen(modifier: Modifier = Modifier) {
    InsertNameScreen(navController = null)
}
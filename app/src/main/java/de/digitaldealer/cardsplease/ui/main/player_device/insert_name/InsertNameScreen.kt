@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package de.digitaldealer.cardsplease.ui.main.player_device.insert_name

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.START_SCREEN
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.main.composables.SimpleDialog
import de.digitaldealer.cardsplease.ui.main.composables.TriggerButton
import de.digitaldealer.cardsplease.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InsertNameScreen(modifier: Modifier = Modifier, navController: NavController?) {

    val viewModel: InsertNameViewModel = viewModel()

    val player by viewModel.player.observeAsState()
    val deckFromFireStore by viewModel.tableFromFireStore.collectAsStateLifecycleAware()
    val isLoading by viewModel.isLoading.collectAsStateLifecycleAware()
    val showHasInternetErrorDialog = remember { mutableStateOf(false) }
    val nickName = remember { mutableStateOf(TextFieldValue()) }
    val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
    val keyboardController = LocalSoftwareKeyboardController.current

    if (!showHasInternetErrorDialog.value) SimpleDialog(
        title = "Überprüfe deine Internetverbindung",
        buttonText = "Ok",
        onDismiss = { showHasInternetErrorDialog.value = false },
        onConfirmClicked = { showHasInternetErrorDialog.value = false }
    )

    LaunchedEffect(key1 = player != null) {
        player?.let {
            val playerJson = Uri.encode(Gson().toJson(player))
            navController?.navigate(route = "$PLAYER_HAND_SCREEN/$playerJson")
        }
    }

    LaunchedEffect(key1 = showHasInternetErrorDialog) {
        viewModel.hasInternet.collectLatest { hasInternet ->
            showHasInternetErrorDialog.value = hasInternet
        }
    }

    LaunchedEffect(key1 = Unit) {
        Logger.debug("call LaunchedEffect: Unit")
        viewModel.onNavigateBack.collectLatest { shouldNavigateBack ->
            if (shouldNavigateBack) navController?.navigate(route = START_SCREEN)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(horizontal = two_GU, vertical = two_GU + half_GU))
        } else {
            Column(
                modifier = Modifier.padding(top = three_GU),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(text = "Tisch: ${deckFromFireStore.tableName}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(one_GU))
                CustomText(text = "SpielId: ${deckFromFireStore.tableId}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(four_GU))
                CustomText(text = "Gib mal deinen Namen ein")
                Spacer(modifier = Modifier.height(four_GU))
                TextField(
                    value = nickName.value,
                    shape = RoundedCornerShape(one_GU),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = { nickName.value = it },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        if (nickName.value.text.isNotBlank()) viewModel.submitToGame(nickName.value.text)
                    }),
                )
                Spacer(modifier = Modifier.height(two_GU))
                TriggerButton(
                    onClick = { if (nickName.value.text.isNotBlank()) viewModel.submitToGame(nickName.value.text) },
                    text = "anmelden"
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_InsertNameScreen(modifier: Modifier = Modifier) {
    InsertNameScreen(navController = null)
}

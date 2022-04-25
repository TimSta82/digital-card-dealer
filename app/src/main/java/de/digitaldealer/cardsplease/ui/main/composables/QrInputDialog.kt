package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.ui.NavigationRoutes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QrInputDialog(
    modifier: Modifier = Modifier,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val qrCode = remember { mutableStateOf(TextFieldValue()) }
    var isError = rememberSaveable { mutableStateOf(false) }
    val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.None)
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(text = "Gib die deckId ein")
                Spacer(modifier = Modifier.height(32.dp))
                TextField(
                    value = qrCode.value,
                    singleLine = true,
                    onValueChange = {
                        if (it.toString().length <= 12) qrCode.value = it
//                        if (it.length <= maxChar) text = it
                        qrCode.value = it
                        isError.value = false
                    },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = KeyboardActions(onDone = {
                        if (isInputValid(qrCode.value.text)) {
                            keyboardController?.hide()
//                            onDismiss
                            navController.navigate(route = "${NavigationRoutes.INSERT_NAME_SCREEN}/${qrCode.value.text.trim()}")
                        } else {
                            isError.value = true
                        }
                    })
                )
                if (isError.value) {
                    keyboardController?.hide()
                    Text(text = "Deine Eingabe ist nicht valide.", style = TextStyle(color = Color.Red))
                }
            }
        },
        buttons = {
            TriggerButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isInputValid(input = qrCode.value.text)) {
                        keyboardController?.hide()
                        navController.navigate(route = "${NavigationRoutes.INSERT_NAME_SCREEN}/${qrCode.value.text.trim()}")
                    }
                },
                text = "Ok"
            )
        }
    )
}

fun isInputValid(input: String): Boolean {
    return (input.isNotBlank() && input.length == 12)
}

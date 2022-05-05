package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QrInputDialog(
    modifier: Modifier = Modifier,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val qrCode = remember { mutableStateOf(TextFieldValue()) }
    val isError = rememberSaveable { mutableStateOf(false) }
    val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.None)
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
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
                    shape = RoundedCornerShape(one_GU),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
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
        confirmButton = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = two_GU),
                horizontalArrangement = Arrangement.Center
            ) {
                TriggerButton(
                    onClick = {
                        if (isInputValid(input = qrCode.value.text)) {
                            keyboardController?.hide()
                            navController.navigate(route = "${NavigationRoutes.INSERT_NAME_SCREEN}/${qrCode.value.text.trim()}")
                        }
                    },
                    text = "Ok"
                )
            }
        }
    )
}

fun isInputValid(input: String): Boolean {
    return (input.isNotBlank() && input.length == 12)
}

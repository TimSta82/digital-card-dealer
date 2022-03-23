package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import de.digitaldealer.cardsplease.ui.main.dealer_device.DealerViewModel
import de.digitaldealer.cardsplease.ui.theme.one_GU
import net.glxn.qrgen.android.QRCode

@Composable
fun AddPlayerDialog(
    modifier: Modifier = Modifier,
    viewModel: DealerViewModel,
    deckId: String,
    tableName: String
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.resetPlayerDeckId()
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tisch: $tableName")
                Spacer(modifier = Modifier.height(one_GU))
                Text(text = "deckId: $deckId")
                Image(
                    bitmap = getQrCodeAsBitmap(deckId = deckId),
                    contentDescription = ""
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = one_GU),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.resetPlayerDeckId() }
                ) {
                    Text("Aha!")
                }
            }
        }
    )
}

@Composable
private fun getQrCodeAsBitmap(deckId: String): ImageBitmap {
    val bitmap = QRCode.from(deckId).withSize(400, 400).bitmap().asImageBitmap()
    return bitmap
}
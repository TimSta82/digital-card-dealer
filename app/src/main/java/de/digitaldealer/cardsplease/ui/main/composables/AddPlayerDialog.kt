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
import androidx.compose.ui.unit.dp
import de.digitaldealer.cardsplease.ui.main.dealer_device.DealerViewModel
import net.glxn.qrgen.android.QRCode

@Composable
fun AddPlayerDialog(
    modifier: Modifier = Modifier,
    viewModel: DealerViewModel,
    addPlayerDeckId: String?
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.resetPlayerDeckId()
        },
        text = {
            addPlayerDeckId?.let {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = getQrCodeAsBitmap(deckId = it),
                        contentDescription = ""
                    )
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.resetPlayerDeckId() }
                ) {
                    Text("Dismiss")
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
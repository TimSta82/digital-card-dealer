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
import de.digitaldealer.cardsplease.ui.main.central_device.CentralViewModel
import net.glxn.qrgen.android.QRCode

@Composable
fun AddPlayerDialog(
    modifier: Modifier = Modifier,
    viewModel: CentralViewModel,
    addPlayerDeckId: String?
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.resetPlayerDeckId()
        },
        title = {
            Text(text = "Scanne mal den Code oder tip in ab -> ${addPlayerDeckId ?: ""}")
        },
        text = {
            addPlayerDeckId?.let {
                Box(contentAlignment = Alignment.Center) {
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
    val bitmap = QRCode.from(deckId).withSize(300, 300).bitmap().asImageBitmap()
    return bitmap
}
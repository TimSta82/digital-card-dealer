package de.digitaldealer.cardsplease.ui.main.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import de.digitaldealer.cardsplease.QR_CODE_SIZE
import de.digitaldealer.cardsplease.ui.theme.one_GU
import net.glxn.qrgen.android.QRCode

@Composable
fun QrCodePlayerDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    tableId: String,
    tableName: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(text = "Tisch: $tableName")
                Spacer(modifier = Modifier.height(one_GU))
                CustomText(text = "tableId: $tableId")
                Spacer(modifier = Modifier.height(one_GU))
                Image(
                    bitmap = getQrCodeAsBitmap(deckId = tableId, Modifier.fillMaxWidth()),
                    contentDescription = ""
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = one_GU)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TriggerButton(
                    onClick = onDismiss,
                    text = "Aha!"
                )
            }
        }
    )
}

@Composable
private fun getQrCodeAsBitmap(deckId: String, fillMaxWidth: Modifier): ImageBitmap {
    val bitmap = QRCode.from(deckId).withSize(QR_CODE_SIZE, QR_CODE_SIZE).bitmap().asImageBitmap()
    return bitmap
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun Preview_QrCodePlayerDialog(modifier: Modifier = Modifier) {
    QrCodePlayerDialog(onDismiss = {}, tableId = "asiudhiuashd", tableName = "Hector")
}

@Composable
@Preview
fun Preview_DialogLight(modifier: Modifier = Modifier) {
    QrCodePlayerDialog(onDismiss = {}, tableId = "asdasda", tableName = "Tablename")
}
package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun TermsOfUsageDialog(
    modifier: Modifier = Modifier,
    onTermsClicked: () -> Unit,
    onPolicyClicked: () -> Unit,
    onConfirm: () -> Unit,
    onDecline: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append("By joining, you agree to the ")

        pushStringAnnotation(tag = "policy", annotation = "https://google.com/policy")
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
            append("privacy policy")
        }
        pop()

        append(" and ")

        pushStringAnnotation(tag = "terms", annotation = "https://google.com/terms")

        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
            append("terms of use")
        }

        pop()
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        text = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(text = "Um die App zu nutzen musst du den AGBs zustimmen")
                Spacer(modifier = modifier.height(one_GU))
                CustomText(text = "blabla")
                Spacer(modifier = modifier.height(one_GU))
                ClickableText(text = annotatedString, style = MaterialTheme.typography.body1, onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                        onPolicyClicked()
                    }

                    annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset).firstOrNull()?.let {
                        onTermsClicked()
                    }
                })
            }
        },
        confirmButton = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = two_GU),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TriggerButton(
                    onClick = onConfirm,
                    text = "Zustimmen"
                )
                Spacer(modifier = modifier.height(one_GU))
                TriggerButton(
                    onClick = onDecline,
                    text = "Nicht zustimmen"
                )
            }
        }
    )
}

@Composable
@Preview
fun Preview_TermsOfUsageDialog(modifier: Modifier = Modifier) {
    TermsOfUsageDialog(
        onConfirm = {},
        onDecline = {},
        onPolicyClicked = {},
        onTermsClicked = {}
    )
}

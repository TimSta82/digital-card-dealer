package de.digitaldealer.cardsplease.ui.terms_of_usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.core.utils.Logger
import de.digitaldealer.cardsplease.ui.MainActivity
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.main.composables.TriggerButton
import de.digitaldealer.cardsplease.ui.main.composables.TwoButtonDialog
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TermsOfUsageScreen(modifier: Modifier = Modifier, navController: NavController) {
    val mainActivity = (LocalContext.current as? MainActivity)
    val viewModel: TermsOfUsageViewModel = viewModel()
    val showCloseAppDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.hasAcceptedTermsOfUsageUseCase.collectLatest { hasAccepted ->
            if (hasAccepted) navController.navigate(route = NavigationRoutes.START_SCREEN)
        }
    }

    if (showCloseAppDialog.value) TwoButtonDialog(
        text = "App schließen oder doch akzeptieren?",
        confirmButtonText = "Akzeptieren",
        declineButtonText = "App schließen",
        onDismiss = { /*TODO*/ },
        onConfirm = viewModel::acceptTermsOfUsage,
        onDecline = { mainActivity?.finish() }
    )

    TermsOfUsageContent(
        modifier = modifier,
        onNavigateToPolicy = {
            navController.navigate(route = NavigationRoutes.DATA_SCREEN)
        },
        onNavigateToTerms = {
            navController.navigate(route = NavigationRoutes.TERMS_OF_USAGE_SCREEN)
        },
        onAcceptTerms = viewModel::acceptTermsOfUsage,
        onShowCloseDialog = { showCloseAppDialog.value = true }
    )
}

@Composable
fun TermsOfUsageContent(
    modifier: Modifier,
    onNavigateToPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onAcceptTerms: () -> Unit,
    onShowCloseDialog: () -> Unit
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
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimary)),
    ) {
        val (title, text, confirm, decline) = createRefs()

        CustomText(
            modifier = modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = four_GU)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = "Nutzungsbedingungen"
        )
        ClickableText(
            modifier = modifier.constrainAs(text) {
                top.linkTo(title.bottom, margin = four_GU)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            style = TextStyle(textAlign = TextAlign.Center, color = Color.White),
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                    onNavigateToPolicy()
                    Logger.debug("policy: ${it.item}")
                }
                annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset).firstOrNull()?.let {
                    onNavigateToTerms()
                    Logger.debug("terms: ${it.item}")
                }
            }
        )
        TriggerButton(
            modifier = modifier.constrainAs(confirm) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(decline.top, margin = two_GU)
            },
            onClick = { onAcceptTerms() },
            text = "Zustimmen"
        )
        TriggerButton(
            modifier = modifier.constrainAs(decline) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = four_GU)
            },
            onClick = { onShowCloseDialog() },
            text = "Nicht zustimmen"
        )
    }
}

@Composable
@Preview
fun Preview_TermsOfUsageContent(modifier: Modifier = Modifier) {
    TermsOfUsageContent(modifier = modifier, onAcceptTerms = {}, onShowCloseDialog = {}, onNavigateToPolicy = {}, onNavigateToTerms = {})
}

package de.digitaldealer.cardsplease.ui.main.start

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.gson.Gson
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.composables.*
import de.digitaldealer.cardsplease.ui.main.dealer_device.findActivity
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel: StartViewModel = viewModel()

    val showPlayerRejoinTableDialog = remember { mutableStateOf(Player()) }
    val showTermsOfUsageDialog = remember { mutableStateOf(true) }

    val shouldSwitchColors by viewModel.alternatingColors.collectAsStateLifecycleAware()

    LaunchedEffect(key1 = Unit) {
        viewModel.localPlayer.collectLatest {
            showPlayerRejoinTableDialog.value = it
        }
    }

    LaunchedEffect(key1 = showTermsOfUsageDialog) {
        viewModel.hasAcceptedTermsOfUsageUseCase.collectLatest { hasAccepted ->
            showTermsOfUsageDialog.value = hasAccepted.not()
        }
    }

    if (showTermsOfUsageDialog.value) TwoButtonDialog(
        text = "Um die App zu nutzen musst du den AGBs zustimmen",
        confirmButtonText = "Zustimmen",
        declineButtonText = "Nicht zustimmen",
        onDismiss = { showTermsOfUsageDialog.value = false },
        onConfirm = viewModel::acceptTermsOfUsage,
        onDecline = { showTermsOfUsageDialog.value = false }
    )

    if (showPlayerRejoinTableDialog.value.tableId != "") TwoButtonDialog(
        text = "Willst du dem Spiel am Tisch ${showPlayerRejoinTableDialog.value.tableName} beitreten?",
        onConfirm = {
            val playerJson = Uri.encode(Gson().toJson(showPlayerRejoinTableDialog.value))
            navController.navigate(route = "${NavigationRoutes.PLAYER_HAND_SCREEN}/$playerJson")
        },
        onDismiss = { showPlayerRejoinTableDialog.value = Player() },
        onDecline = viewModel::onDeclineJoinTable,
        confirmButtonText = "Betreten",
        declineButtonText = "Ablehnen"
    )

    Scaffold(
        scaffoldState = scaffoldState,
        drawerShape = drawerShape(),
        drawerContent = {
            StartDrawer(navController = navController, context = context)
        },
        // Defaults to true
        drawerGesturesEnabled = true,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Info") },
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            )
        }
    ) {
        StartContent(
            shouldSwitchColors = shouldSwitchColors,
            onStartAsDealer = {
                navController.navigate(route = NavigationRoutes.DEALER_DEVICE_START_SCREEN)
            },
            onStartAsPlayer = {
                navController.navigate(route = NavigationRoutes.PLAYER_DEVICE_START_SCREEN)
            }
        )
    }
}

@Composable
fun StartDrawer(modifier: Modifier = Modifier, navController: NavController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(two_GU)
    ) {
        Row(
            modifier = Modifier.padding(two_GU),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_clubs), contentDescription = "")
            CustomText(text = "Cards Please", modifier = Modifier.padding(16.dp))
        }
        Divider()
        DrawerItem(text = "Über die App", icon = Icons.Filled.DeviceUnknown) {
            navController.navigate(route = NavigationRoutes.IMPRINT_SCREEN)
        }
        Spacer(modifier = Modifier.height(one_GU))
        DrawerItem(text = "Datenschutz", icon = Icons.Filled.DataExploration) {
            navController.navigate(route = NavigationRoutes.DATA_SCREEN)
        }
        Spacer(modifier = Modifier.height(one_GU))
        DrawerItem(text = "Credits", icon = Icons.Filled.FrontHand) {
            navController.navigate(route = NavigationRoutes.CREDITS_SCREEN)
        }
        DrawerItem(text = "Lizenzen", icon = Icons.Filled.Book) {
            context.findActivity()?.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }
    }
}

@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onNavigate: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(two_GU)
            .clickable { onNavigate() }
    ) {
        Icon(icon, contentDescription = "")
        CustomText(text = text, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun StartContent(
    shouldSwitchColors: Boolean,
    onStartAsDealer: () -> Unit,
    onStartAsPlayer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
//            .background(color = colorResource(id = R.color.colorPrimary)),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = four_GU)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = two_GU)
            ) {
                Icon(
                    modifier = Modifier.weight(1f, fill = true),
                    painter = painterResource(id = R.drawable.ic_clubs2),
                    tint = colorResource(id = if (shouldSwitchColors) R.color.card_red else R.color.black),
                    contentDescription = ""
                )
                Icon(
                    modifier = Modifier.weight(1f, fill = true),
                    painter = painterResource(id = R.drawable.ic_hearts2),
                    tint = colorResource(id = if (shouldSwitchColors) R.color.black else R.color.card_red),
                    contentDescription = ""
                )
                Icon(
                    modifier = Modifier.weight(1f, fill = true),
                    painter = painterResource(id = R.drawable.ic_spades2),
                    tint = colorResource(id = if (shouldSwitchColors) R.color.card_red else R.color.black),
                    contentDescription = ""
                )
                Icon(
                    modifier = Modifier.weight(1f, fill = true),
                    painter = painterResource(id = R.drawable.ic_diamonds2),
                    tint = colorResource(id = if (shouldSwitchColors) R.color.black else R.color.card_red),
                    contentDescription = ""
                )
            }
            CustomText(
                modifier = Modifier.padding(horizontal = two_GU, vertical = two_GU),
                text = "Cards Please soll euch das gemeinsame Pokern am Tisch erleichtern, indem es Karten mischt und dealt. Hierzu benötigt ihr ein Handy oder Tablet, welches als Dealer fungiert" +
                    ".\nDanach kann jeder Spieler mit seinem Handy ganz einfach einem Spiel beitreten. \nViel Spass und gute Karten!"
            )
            Spacer(modifier = Modifier.height(four_GU))
            EntryCard(
                entryType = EntryType.DEALER,
                content = {
                    EntryContent(entryType = EntryType.DEALER) { onStartAsDealer() }
                },
                onClick = { onStartAsDealer() }
            )
            Spacer(modifier = Modifier.height(two_GU))
            EntryCard(
                entryType = EntryType.PLAYER,
                content = {
                    EntryContent(entryType = EntryType.PLAYER) { onStartAsPlayer() }
                },
                onClick = { onStartAsPlayer() }
            )
            Spacer(modifier = Modifier.height(two_GU))
        }
    }
}

fun drawerShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, right = size.width / 3 * 2, bottom = size.height))
    }
}

fun bottomSheetShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                topLeftCornerRadius = CornerRadius(0.5f),
                topRightCornerRadius = CornerRadius(0.5f)
            )
        )
    }
}

@Preview
@Composable
fun Preview_StartContent(modifier: Modifier = Modifier) {
    StartContent(onStartAsDealer = {}, onStartAsPlayer = {}, shouldSwitchColors = false)
}

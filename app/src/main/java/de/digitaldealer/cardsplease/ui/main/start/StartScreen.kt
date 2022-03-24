package de.digitaldealer.cardsplease.ui.main.start

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.main.composables.EntryCard
import de.digitaldealer.cardsplease.ui.main.composables.EntryContent
import de.digitaldealer.cardsplease.ui.main.composables.EntryType
import de.digitaldealer.cardsplease.ui.main.dealer_device.findActivity
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import kotlinx.coroutines.launch

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
        StartContent(navController = navController)
    }
}

@Composable
fun StartDrawer(modifier: Modifier = Modifier, navController: NavController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.colorAccent))
            .padding(two_GU)
    ) {
        Row(
            modifier = Modifier.padding(two_GU),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_clubs), contentDescription = "")
            Text("Cards Please", modifier = Modifier.padding(16.dp))
        }
        Divider()
        DrawerItem(text = "Imprint", icon = Icons.Filled.DeviceUnknown) {
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
fun DrawerItem(modifier: Modifier = Modifier, text: String, icon: ImageVector, onNavigate: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(two_GU)
            .clickable { onNavigate() }
    ) {
        Icon(icon, contentDescription = "")
        Text(text = text, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun StartContent(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimary)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = four_GU, vertical = two_GU)
                .verticalScroll(rememberScrollState())
        ) {
            CustomText(
                modifier = Modifier.padding(horizontal = four_GU, vertical = two_GU),
                text = "Cards Please soll euch das gemeinsame Pokern am Tisch erleichtern, indem es Karten mischt und dealt. Hierzu benötigt ihr ein Handy oder Tablet, welches als Dealer fungiert" +
                    ".\nDanach kann jeder Spieler mit seinem Handy ganz einfach einem Spiel beitreten. \nViel Spass und gute Karten!"
            )
            Spacer(modifier = Modifier.height(four_GU))
            EntryCard(entryType = EntryType.DEALER, content = {
                EntryContent(entryType = EntryType.DEALER) {
                    navController.navigate(route = NavigationRoutes.DEALER_DEVICE_START_SCREEN)
                }
            }, onClick = { navController.navigate(route = NavigationRoutes.DEALER_DEVICE_START_SCREEN) })
            Spacer(modifier = Modifier.height(two_GU))
            EntryCard(entryType = EntryType.PLAYER, content = {
                EntryContent(entryType = EntryType.PLAYER) {
                    navController.navigate(route = NavigationRoutes.PLAYER_DEVICE_START_SCREEN)
                }
            }, onClick = { navController.navigate(route = NavigationRoutes.PLAYER_DEVICE_START_SCREEN) })
        }
    }
}

fun drawerShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, right = size.width / 2, bottom = size.height))
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
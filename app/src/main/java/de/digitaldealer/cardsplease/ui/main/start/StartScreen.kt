package de.digitaldealer.cardsplease.ui.main.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.NavigationRoutes
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.main.composables.EntryCard
import de.digitaldealer.cardsplease.ui.main.composables.EntryType
import de.digitaldealer.cardsplease.ui.theme.four_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import kotlinx.coroutines.launch

@Composable
fun StartScreen(modifier: Modifier = Modifier, navController: NavController) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerShape = customShape(),
        drawerContent = {
            StartDrawer()
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
fun StartDrawer(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth(0.5f)) {
        Text("Cards Please", modifier = Modifier.padding(16.dp))
        Divider()
        Text("Impressum", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(one_GU))
        Text("Datenschutz", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(one_GU))
        Text("Credits", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(one_GU))
        Text("<a href=\"https://www.vecteezy.com/free-vector/cartoon\">Cartoon Vectors by Vecteezy</a>", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(one_GU))
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
        Column(modifier = Modifier.padding(four_GU)) {
            CustomText(
                modifier = Modifier.padding(four_GU),
                text = "Cards Please soll euch das gemeinsame Pokern am Tisch erleichtern, indem es Karten mischt und dealt. Hierzu benötigt ihr ein Handy oder Tablet, welches als Dealer fungiert" +
                    ".\nDanach kann jeder Spieler mit seinem Handy ganz einfach einem Spiel beitreten. \nViel Spass und gute Karten!"
            )
            Spacer(modifier = Modifier.height(four_GU))
            EntryCard(entryType = EntryType.DEALER, onClick = { navController.navigate(route = NavigationRoutes.DEALER_DEVICE_START_SCREEN) })
            Spacer(modifier = Modifier.height(two_GU))
            EntryCard(entryType = EntryType.PLAYER, onClick = { navController.navigate(route = NavigationRoutes.PLAYER_DEVICE_START_SCREEN) })
        }
    }
}

fun customShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, right = size.width / 2, bottom = size.height))
    }
}
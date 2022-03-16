package de.digitaldealer.cardsplease.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.digitaldealer.cardsplease.ui.NavigationRoutes.CENTRAL_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.MAIN_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.INSERT_NAME_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.SATELLITE_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.START_SCREEN
import de.digitaldealer.cardsplease.ui.base.BaseActivity
import de.digitaldealer.cardsplease.ui.main.MainScreen
import de.digitaldealer.cardsplease.ui.main.MainViewModel
import de.digitaldealer.cardsplease.ui.main.central_device.LockScreenOrientation
import de.digitaldealer.cardsplease.ui.main.satellite_device.player_hand.PlayerHandScreen
import de.digitaldealer.cardsplease.ui.main.satellite_device.SatelliteDeviceStartScreen
import de.digitaldealer.cardsplease.ui.main.satellite_device.insert_name.InsertNameScreen
import de.digitaldealer.cardsplease.ui.main.start.StartScreen
import de.digitaldealer.cardsplease.ui.theme.DefaultTheme

class MainActivity : BaseActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultApp(mainViewModel)
        }
    }
}

@Composable
fun DefaultApp(mainViewModel: MainViewModel) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()

    DefaultTheme {
        Scaffold(
            scaffoldState = scaffoldState,
//            topBar = {
//                TopAppBar(
//                    title = { Text(stringResource(id = R.string.app_name)) },
//                    contentColor = Color.White,
//                    navigationIcon = {
//                        IconButton(onClick = {}) {
//                            Icon(painter = painterResource(id = R.drawable.ic_baseline_android_24), contentDescription = null, tint = Color.White)
//                        }
//                    }
//                )
//            },
        ) {
            NavHost(navController = navController, startDestination = START_SCREEN) {
                composable(route = MAIN_SCREEN) {
                    MainScreen(
                        viewModel = mainViewModel,
                        scaffoldState = scaffoldState
                    )
                }
                composable(START_SCREEN) {
                    StartScreen(navController = navController)
                }
                composable(CENTRAL_DEVICE_START_SCREEN) {
                    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
//                    CentralDeviceStartScreen()
                }
                composable(SATELLITE_DEVICE_START_SCREEN) {
                    SatelliteDeviceStartScreen(navController = navController)
                }
                composable(route = "$INSERT_NAME_SCREEN/{deckId}",
                    arguments = listOf(
                        navArgument("deckId") {
                            type = NavType.StringType
                        }
                    )
                ) {
                    InsertNameScreen(navController = navController)
                }
                composable(
                    route = "$PLAYER_HAND_SCREEN/{deckId}/{nickName}",
                    arguments = listOf(
                        navArgument("deckId") {
                            type = NavType.StringType
                        },
                        navArgument("nickName") {
                            type = NavType.StringType
                        },
                    )
                ) {
                    PlayerHandScreen(navController = navController)
                }
            }
        }
    }
}

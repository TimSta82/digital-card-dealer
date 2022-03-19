package de.digitaldealer.cardsplease.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes.DEALER_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.INSERT_NAME_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.START_SCREEN
import de.digitaldealer.cardsplease.ui.base.BaseActivity
import de.digitaldealer.cardsplease.ui.main.dealer_device.LockScreenOrientation
import de.digitaldealer.cardsplease.ui.main.player_device.PlayerStartScreen
import de.digitaldealer.cardsplease.ui.main.player_device.insert_name.InsertNameScreen
import de.digitaldealer.cardsplease.ui.main.player_device.player_hand.PlayerHandScreen
import de.digitaldealer.cardsplease.ui.main.start.StartScreen
import de.digitaldealer.cardsplease.ui.theme.DefaultTheme

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultApp()
        }
    }
}

@Composable
fun DefaultApp() {
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
                composable(START_SCREEN) {
                    StartScreen(navController = navController)
                }
                composable(DEALER_DEVICE_START_SCREEN) {
                    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
                }
                composable(PLAYER_DEVICE_START_SCREEN) {
                    PlayerStartScreen(navController = navController)
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
                    route = "$PLAYER_HAND_SCREEN/{player}",
                    arguments = listOf(
                        navArgument("player") {
                            type = PlayerNavParamType()
                        }
                    )
                ) {
                    PlayerHandScreen()
                }
            }
        }
    }
}

class PlayerNavParamType : NavType<Player>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Player? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Player {
        return Gson().fromJson(value, Player::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Player) {
        bundle.putParcelable(key, value)
    }
}

package de.digitaldealer.cardsplease.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import de.digitaldealer.cardsplease.domain.model.Player
import de.digitaldealer.cardsplease.ui.NavigationRoutes.CREDITS_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.DATA_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.DEALER_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.IMPRINT_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.INSERT_NAME_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.NAV_ARG_PLAYER
import de.digitaldealer.cardsplease.ui.NavigationRoutes.NAV_ARG_TABLE_ID
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_DEVICE_START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.PLAYER_HAND_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.START_SCREEN
import de.digitaldealer.cardsplease.ui.NavigationRoutes.TERMS_OF_USAGE_SCREEN
import de.digitaldealer.cardsplease.ui.base.BaseActivity
import de.digitaldealer.cardsplease.ui.extensions.collectAsStateLifecycleAware
import de.digitaldealer.cardsplease.ui.main.dealer_device.LockScreenOrientation
import de.digitaldealer.cardsplease.ui.main.player_device.PlayerStartScreen
import de.digitaldealer.cardsplease.ui.main.player_device.insert_name.InsertNameScreen
import de.digitaldealer.cardsplease.ui.main.player_device.player_hand.PlayerHandScreen
import de.digitaldealer.cardsplease.ui.main.start.StartScreen
import de.digitaldealer.cardsplease.ui.main.start.legal.CreditsScreen
import de.digitaldealer.cardsplease.ui.main.start.legal.DataScreen
import de.digitaldealer.cardsplease.ui.main.start.legal.ImprintScreen
import de.digitaldealer.cardsplease.ui.terms_of_usage.TermsOfUsageScreen
import de.digitaldealer.cardsplease.ui.terms_of_usage.TermsOfUsageViewModel
import de.digitaldealer.cardsplease.ui.theme.MainTheme

class MainActivity : BaseActivity() {

    companion object {
        fun startActivity(origin: Activity) {
            origin.startActivity(Intent(origin, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardsPleaseApp()
        }
    }
}

@Composable
fun CardsPleaseApp() {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val termsOfUsageViewModel: TermsOfUsageViewModel = viewModel()
    val hasAccepted by termsOfUsageViewModel.hasAcceptedTermsOfUsageUseCase.collectAsStateLifecycleAware()

    MainTheme {
        Scaffold(
            scaffoldState = scaffoldState,
        ) {
            NavHost(navController = navController, startDestination = if (hasAccepted) START_SCREEN else TERMS_OF_USAGE_SCREEN) {
                composable(TERMS_OF_USAGE_SCREEN) {
                    TermsOfUsageScreen(navController = navController)
                }
                composable(START_SCREEN) {
                    StartScreen(navController = navController)
                }
                composable(DEALER_DEVICE_START_SCREEN) {
                    LockScreenOrientation(
                        orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
                        navController = navController
                    )
                }
                composable(PLAYER_DEVICE_START_SCREEN) {
                    PlayerStartScreen(navController = navController)
                }
                composable(
                    route = "$INSERT_NAME_SCREEN/{$NAV_ARG_TABLE_ID}",
                    arguments = listOf(
                        navArgument(NAV_ARG_TABLE_ID) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    InsertNameScreen(navController = navController)
                }
                composable(
                    route = "$PLAYER_HAND_SCREEN/{$NAV_ARG_PLAYER}",
                    arguments = listOf(
                        navArgument(NAV_ARG_PLAYER) {
                            type = PlayerNavParamType()
                        }
                    )
                ) {
                    PlayerHandScreen(navController = navController)
                }
                composable(IMPRINT_SCREEN) {
                    ImprintScreen()
                }
                composable(DATA_SCREEN) {
                    DataScreen()
                }
                composable(CREDITS_SCREEN) {
                    CreditsScreen()
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

package de.bornholdtlee.defaultprojectkotlin.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.ui.NavigationRoutes.CENTRAL_DEVICE_START_SCREEN
import de.bornholdtlee.defaultprojectkotlin.ui.NavigationRoutes.MAIN_SCREEN
import de.bornholdtlee.defaultprojectkotlin.ui.NavigationRoutes.SATELLITE_DEVICE_START_SCREEN
import de.bornholdtlee.defaultprojectkotlin.ui.NavigationRoutes.START_SCREEN
import de.bornholdtlee.defaultprojectkotlin.ui.base.BaseActivity
import de.bornholdtlee.defaultprojectkotlin.ui.main.MainScreen
import de.bornholdtlee.defaultprojectkotlin.ui.main.MainViewModel
import de.bornholdtlee.defaultprojectkotlin.ui.main.central_device.CentralDeviceStartScreen
import de.bornholdtlee.defaultprojectkotlin.ui.main.central_device.LockScreenOrientation
import de.bornholdtlee.defaultprojectkotlin.ui.main.satellite_device.SatelliteDeviceStartScreen
import de.bornholdtlee.defaultprojectkotlin.ui.main.start.StartScreen
import de.bornholdtlee.defaultprojectkotlin.ui.theme.DefaultTheme

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
                    SatelliteDeviceStartScreen()
                }
            }
        }
    }
}

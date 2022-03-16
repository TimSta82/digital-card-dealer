package de.digitaldealer.cardsplease.ui.main.satellite_device

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import de.digitaldealer.cardsplease.ui.NavigationRoutes.INSERT_NAME_SCREEN
import de.digitaldealer.cardsplease.ui.main.satellite_device.qr_scanner.QrScannerScreen

@Composable
fun SatelliteDeviceStartScreen(modifier: Modifier = Modifier, navController: NavController) {
//    FeatureThatRequiresCameraPermission(navController)
    QrScannerScreen(navController)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermission(navController: NavController) {

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
//            ManualInputContainer(viewModel)
                    AddPlayerQrScanScreen(navController = navController)
                }
            }
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow =
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Camera permission required for this feature to be available. " +
                        "Please grant the permission"

                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}

@Composable
fun AddPlayerQrScanScreen(navController: NavController) {
    val context = LocalContext.current
    var scanFlag by remember { mutableStateOf(false) }

    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous { result ->
                if (scanFlag) {
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let { barCodeOrQr ->
                    //Do something and when you finish this something

                    navController.navigate(route = "$INSERT_NAME_SCREEN/$barCodeOrQr")
                    //put scanFlag = false to scan another item
//                    scanFlag = false
                }
                //If you don't put this scanFlag = false, it will never work again.
                //you can put a delay over 2 seconds and then scanFlag = false to prevent multiple scanning

            }
        }
    }

    AndroidView(
        modifier = Modifier,
        factory = { compoundBarcodeView },
    )
}
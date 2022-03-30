package de.digitaldealer.cardsplease.ui.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.ui.MainActivity
import de.digitaldealer.cardsplease.ui.theme.twenty_GU
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    companion object {
        private const val SPLASH_SCREEN_TIME = 1_000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LaunchedEffect(key1 = Unit) {
                delay(SPLASH_SCREEN_TIME)
                MainActivity.startActivity(this@SplashActivity)
            }
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .width(twenty_GU)
                .height(twenty_GU),
            painter = painterResource(id = R.drawable.ic_clubs),
            contentDescription = "App Logo"
        )
    }
}
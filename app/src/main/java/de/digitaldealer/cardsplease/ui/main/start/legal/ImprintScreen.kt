package de.digitaldealer.cardsplease.ui.main.start.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.digitaldealer.cardsplease.BuildConfig
import de.digitaldealer.cardsplease.ui.main.composables.CustomText
import de.digitaldealer.cardsplease.ui.theme.two_GU

@Composable
fun ImprintScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { CustomText(text = "Über die App - Cards Please") }
//        Spacer(modifier = modifier.height(two_GU))
        item { CustomText(text = "Herausgeber") }
        item { CustomText(text = "verantwortlich nach § 5 Abs. 1 TMG, § 18 Abs. 1") }
        item { CustomText(text = "Tim Staats\nLachmannweg 4\n22589 Hamburg\nDeutschland") }
        item { CustomText(text = "Kontakt: timdev@mail.de") }
        item { CustomText(text = "Freunde treffen - jaaaa\nPokern... - cool\nmit echten Chips - klappert so schön\nKarten mischen und verteilen... - lame! Mach ich nicht!\n\nLass doch einfach Cards Please nehmen!") }
        item { Spacer(modifier = modifier.height(two_GU)) }
        item { CustomText(text = "Haftungsausschluss") }
        item { CustomText(text = "Diese App verwendet Firebase für die synchronisation zwischen Dealer und Spieler Gerät. Hierzu wird eine zufällig generierte ID hochgeladen. Über diese ID werden " +
            "die Karten vom Dealer zum Spieler zugeteilt.") }
        item { CustomText(text = "Version ${BuildConfig.VERSION_NAME}") }
    }
}

@Composable
fun Preview_ImprintScreen(modifier: Modifier = Modifier) {
    ImprintScreen()
}
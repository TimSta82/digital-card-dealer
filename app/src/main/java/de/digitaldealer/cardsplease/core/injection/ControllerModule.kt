package de.digitaldealer.cardsplease.core.injection

import de.digitaldealer.cardsplease.data.controller.NetworkStateController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val controllerModule = module {

    single { NetworkStateController(androidContext()) }
}

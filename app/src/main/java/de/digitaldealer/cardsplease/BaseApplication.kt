package de.digitaldealer.cardsplease

import android.app.Application
import de.digitaldealer.cardsplease.data.controller.NetworkStateController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BaseApplication : Application(), KoinComponent {
    private val networkStateController by inject<NetworkStateController>()

    override fun onCreate() {
        super.onCreate()
        networkStateController.register()
    }
}

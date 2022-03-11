package de.digitaldealer.cardsplease.core.android.app_startup

import android.content.Context
import androidx.startup.Initializer
import de.digitaldealer.cardsplease.core.injection.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {

    companion object {
        val modules = repositoryModule + useCaseModule + dataModule + networkModule + helperModule
    }

    override fun create(context: Context): KoinApplication {
        return startKoin {
            androidContext(context)
            modules(modules)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}

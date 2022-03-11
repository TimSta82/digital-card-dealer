package de.digitaldealer.cardsplease.core.injection

import de.digitaldealer.cardsplease.core.utils.CameraUtils
import de.digitaldealer.cardsplease.core.utils.ImageUtils
import org.koin.dsl.module

val helperModule = module {

    factory { CameraUtils() }
    factory { ImageUtils() }
}

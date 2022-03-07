package de.bornholdtlee.defaultprojectkotlin.core.injection

import de.bornholdtlee.defaultprojectkotlin.core.utils.CameraUtils
import de.bornholdtlee.defaultprojectkotlin.core.utils.ImageUtils
import org.koin.dsl.module

val helperModule = module {

    factory { CameraUtils() }
    factory { ImageUtils() }
}

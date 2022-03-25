package de.digitaldealer.cardsplease.ui.main.start

import androidx.lifecycle.ViewModel
import de.digitaldealer.cardsplease.ui.extensions.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartViewModel : ViewModel() {

    private val _alternatingColors = MutableStateFlow(true)
    val alternatingColors = _alternatingColors.asStateFlow()

    init {
        launch {
            while (true) {
                delay(1000L)
                _alternatingColors.value = _alternatingColors.value.not()
            }
        }
    }
}

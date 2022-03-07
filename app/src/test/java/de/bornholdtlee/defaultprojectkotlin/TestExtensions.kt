package de.bornholdtlee.defaultprojectkotlin

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.flow.StateFlow

/**
 * Skips the initial value by consuming the initial value
 */
internal suspend fun <T> StateFlow<T>.testStateFlow(
    timeoutMs: Long = 1_000L,
    validate: suspend FlowTurbine<T>.() -> Unit
) {
    test(timeoutMs) {
        awaitItem() // Consume initial state value
        validate()
    }
}

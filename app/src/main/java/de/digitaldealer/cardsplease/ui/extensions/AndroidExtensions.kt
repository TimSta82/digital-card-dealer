package de.digitaldealer.cardsplease.ui.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.concurrent.Executor

fun ViewModel.launch(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(dispatcher) { block(this) }
fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch { block(this) }

fun Context.getHexFromColorRes(@ColorRes colorRes: Int) = String.format("#%06x", ContextCompat.getColor(this, colorRes) and 0xffffff)

inline fun <T> Flow<T>.collectIn(
    owner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collect { action(it) } } }

inline fun <T> Flow<T>.collectLatestIn(
    owner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collectLatest { action(it) } } }

fun Context.assetToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/**
 *  @param started Parameter stopTimeoutMillis is set to 5 seconds to let the flow survive a configuration change
 */
fun <T> ViewModel.stateFlow(
    flow: Flow<T>,
    initialValue: T,
    scope: CoroutineScope = viewModelScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L)
): StateFlow<T> = flow.stateIn(
    scope = scope,
    started = started,
    initialValue = initialValue
)

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

inline fun <reified T : ViewModel> Fragment.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelInitialization.invoke() as T
            }
        }
    }
}

inline fun <reified T : ViewModel> AppCompatActivity.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelInitialization.invoke() as T
            }
        }
    }
}

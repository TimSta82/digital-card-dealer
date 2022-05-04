package de.digitaldealer.cardsplease.ui.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import de.digitaldealer.cardsplease.core.utils.Logger
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

suspend fun AlertDialog.await(
    positiveText: String,
    negativeText: String
) = suspendCancellableCoroutine<Boolean> { cont ->
    val listener = DialogInterface.OnClickListener { _, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) cont.resume(value = true, onCancellation = { Logger.debug("onCancellation: $it") })
        else if (which == AlertDialog.BUTTON_NEGATIVE) cont.resume(value = false, onCancellation = { Logger.debug("onCancellation: $it") })
    }

    setButton(AlertDialog.BUTTON_POSITIVE, positiveText, listener)
    setButton(AlertDialog.BUTTON_NEGATIVE, negativeText, listener)

    // we can either decide to cancel the coroutine if the dialog
    // itself gets cancelled, or resume the coroutine with the
    // value [false]
    setOnCancelListener { cont.cancel() }

    // if we make this coroutine cancellable, we should also close the
    // dialog when the coroutine is cancelled
    cont.invokeOnCancellation { dismiss() }

    // remember to show the dialog before returning from the block,
    // you won't be able to do it after this function is called!
    show()
}
